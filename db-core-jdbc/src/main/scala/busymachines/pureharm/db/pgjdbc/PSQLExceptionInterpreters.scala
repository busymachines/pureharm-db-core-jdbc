/*
 * Copyright 2019 BusyMachines
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package busymachines.pureharm.db.pgjdbc

import cats._
import cats.syntax.all._
import busymachines.pureharm.db._
import org.postgresql.util._

/** @author
  *   Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02
  *   Jul 2020
  */
object PSQLExceptionInterpreters {

  object PSQLStates {
    val NotNullViolation:    String = PSQLState.NOT_NULL_VIOLATION.getState
    val ForeignKeyViolation: String = PSQLState.FOREIGN_KEY_VIOLATION.getState
    val UniqueViolation:     String = PSQLState.UNIQUE_VIOLATION.getState
  }

  private[pgjdbc] object PSQLErrorParsers {
    import atto._, Atto._
    private val underscore  = char('_')
    private val closeParens = char(')')
    private val doubleQuote = char('"')
    private val column: Parser[String] = many1(letterOrDigit | underscore).map(_.mkString_(""))

    private val untilNextDoubleQuote: Parser[String] =
      manyUntil(anyChar, doubleQuote).map(_.mkString(""))

    /** Consumes the last parenthesis as well
      */
    private def untilLastClosedParens: Parser[String] = for {
      head      <- manyUntil(anyChar, closeParens).map(_.mkString(""))
      remaining <- get
      tail      <- if (remaining.contains(')')) untilLastClosedParens else "".pure[Parser]
    } yield s"$head$tail"

    /** usually has the value of format:
      * {{{
      *   Key (id)=(row1_id) already exists.
      * }}}
      */
    private[pgjdbc] object unique {

      private val parser: Parser[(String, String)] = for {
        _          <- string("Key (")
        columnName <- column
        _          <- string(")=(")
        value      <- untilLastClosedParens
        _          <- string(" already exists.")
      } yield (columnName, value)

      def apply[F[_]: MonadThrow](s: String): F[(String, String)] =
        parser.parse(s).done.either.leftMap(s => new RuntimeException(s): Throwable).liftTo[F]

    }

    /** example:
      * {{{
      *   Key (row_id)=(120-3921-039213) is not present in table "pureharm_rows".
      * }}}
      */
    private[pgjdbc] object foreignKey {

      private val parser: Parser[(String, String, String)] = for {
        _          <- string("Key (")
        columnName <- column
        _          <- string(")=(")
        value      <- untilLastClosedParens
        _          <- untilNextDoubleQuote
        table      <- untilNextDoubleQuote
      } yield (columnName, value, table)

      def apply[F[_]: MonadThrow](s: String): F[(String, String, String)] =
        parser.parse(s).done.either.leftMap(s => new RuntimeException(s): Throwable).liftTo[F]
    }

  }

  /** Only call when org.postgresql.util.PSQLException#getSQLState == org.postgresql.util.PSQLStates.UniqueViolation]]
    *
    * Will attempt to extract the values of the state by doing regex over the error message... yey, java?
    */
  def uniqueKey[F[_]: MonadThrow](e: PSQLException): F[DBUniqueConstraintViolationAnomaly] =
    PSQLExceptionInterpreters.PSQLErrorParsers
      .unique[F](e.getServerErrorMessage.getDetail)
      .map(t => DBUniqueConstraintViolationAnomaly(t._1, t._2))

  /** Only call when org.postgresql.util.PSQLException#getSQLState == org.postgresql.util.PSQLStates.ForeignKeyViolation
    *
    * Will attempt to extract the values of the state by doing regex over the error message... yey, java?
    */
  def foreignKey[F[_]: MonadThrow](e: PSQLException): F[DBForeignKeyConstraintViolationAnomaly] = {
    val msg = e.getServerErrorMessage
    for {
      table      <- Option(msg.getTable).liftTo[F](e)
      constraint <- Option(msg.getConstraint).liftTo[F](e)
      t          <- PSQLErrorParsers.foreignKey[F](msg.getDetail)
      (column, value, fTable) = t
    } yield DBForeignKeyConstraintViolationAnomaly(
      table        = table,
      constraint   = constraint,
      column       = column,
      value        = value,
      foreignTable = fTable,
    )
  }

  lazy val adapt: PartialFunction[Throwable, Throwable] = {
    case e: PSQLException =>
      e.getSQLState match {
        case PSQLStates.UniqueViolation     => uniqueKey[Either[Throwable, *]](e).getOrElse(e)
        case PSQLStates.ForeignKeyViolation => foreignKey[Either[Throwable, *]](e).getOrElse(e)
        case _                              => e
      }
    case e => e
  }
}
