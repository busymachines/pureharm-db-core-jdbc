# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

# unreleased

This is the first release that is also available for Scala 3!

### dependency upgrades

- [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.3.0`
- [pureharm-db-core](https://github.com/busymachines/pureharm-db-core/releases) `0.5.0`
- [pgjdbc](https://github.com/pgjdbc/pgjdbc/releases) `42.2.23`

### internals
- bump scalafmt to `3.0.0-RC6` — from `2.7.5`
- bump sbt to `1.5.5`
- bump sbt-spiewak to `0.21.0`

# 0.4.0

### dependency upgrades

- [pureharm-db-core](https://github.com/busymachines/pureharm-db-core/releases) `0.4.0`

# 0.3.0

### dependency upgrades

- [pureharm-core-anomaly](https://github.com/busymachines/pureharm-core/releases) `0.2.0`
- [pureharm-db-core](https://github.com/busymachines/pureharm-db-core/releases) `0.3.0`

# 0.2.0

- remove dependency on `pureharm-effect-cats` in `Compile`, now only in `Test`

### dependency upgrades

- bump pureharm to `0.2.0`
- bump log4cats to `1.2.2`
- bump atto to `0.9.3`

# 0.1.0

Split out from [pureharm](https://github.com/busymachines/pureharm) as of version `0.0.7`.

:warning: Breaking changes :warning:

- renamed module maven artifact ID from `pureharm-db-core-psql` to `pureharm-db-core-jdbc`
- renamed package `busymachines.pureharm.db.psql` -> `busymachines.pureharm.db.pgjdbc`
