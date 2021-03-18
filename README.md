# pureharm-db-core-jdbc

See [changelog](./CHANGELOG.md).

We do not even pretend to support anything other than Postgresql.

Utility library to be used in other pureharm modules:

- `pureharm-db-flyway`
- `pureharm-db-doobie`
- `pureharm-db-slick`
- and other modules that depend on JDBC.

## modules

- `"com.busymachines" %% s"pureharm-db-flyway" % "0.1.0"`. Which has these as its main dependencies:
  - [pgjdbc](https://github.com/pgjdbc/pgjdbc/releases) `42.2.19`
  - [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.1.0`
  - [pureharm-effects-cats](https://github.com/busymachines/pureharm-effects-cats/releases) `0.1.0`
  - [pureharm-db-core](https://github.com/busymachines/pureharm-db-core/releases) `0.1.0`

## usage

Under construction. See [release notes](https://github.com/busymachines/pureharm-db-core/releases) and tests for examples.

## Copyright and License

All code is available to you under the Apache 2.0 license, available
at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) and also in
the [LICENSE](./LICENSE) file.
