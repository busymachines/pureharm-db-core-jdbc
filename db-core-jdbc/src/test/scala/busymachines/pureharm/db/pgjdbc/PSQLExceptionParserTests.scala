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

import cats.effect.IO
import busymachines.pureharm.testkit._
import org.typelevel.log4cats.slf4j._

/** @author
  *   Lorand Szakacs, https://github.com/lorandszakacs
  * @since 02
  *   Jul 2020
  */
final class PSQLExceptionParserTests extends PureharmTest {

  private val parsers = PSQLExceptionInterpreters.PSQLErrorParsers
  override val testLogger: TestLogger = TestLogger(Slf4jLogger.getLogger[IO])

  test("unique") {
    for {
      attempt <- parsers.unique[IO]("Key (id)=(row1_id) already exists.").attempt
    } yield assertSuccess(attempt)(("id", "row1_id"))
  }

  test("foreign") {
    for {
      attempt <-
        parsers.foreignKey[IO]("""Key (row_id)=(120-3921-039213) is not present in table "pureharm_rows".""").attempt
    } yield assertSuccess(attempt)(("row_id", "120-3921-039213", "pureharm_rows"))
  }
}
