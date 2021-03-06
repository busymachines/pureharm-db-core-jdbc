# pureharm-db-core-jdbc

See [changelog](./CHANGELOG.md).

We do not even pretend to support anything other than Postgresql.

Utility library to be used in other pureharm modules:

- `pureharm-db-flyway`
- `pureharm-db-doobie`
- `pureharm-db-slick`
- and other modules that depend on JDBC.

## Scala versions

- Scala `2.13`: JVM
- Scala `3`: JVM

## modules

- `"com.busymachines" %% s"pureharm-db-core-jdbc" % "0.6.0"`. Which has these as its main dependencies:
  - [pgjdbc](https://github.com/pgjdbc/pgjdbc/releases) `42.2.23`
  - [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.3.0`
  - [pureharm-db-core](https://github.com/busymachines/pureharm-db-core/releases) `0.5.0`

## usage

Under construction. See [release notes](https://github.com/busymachines/pureharm-db-core-jdbc/releases) and tests for examples.

## Copyright and License

All code is available to you under the Apache 2.0 license, available
at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in
the [LICENSE](./LICENSE) file.
