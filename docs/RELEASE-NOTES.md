


## Release Notes

| version | release date | Java |
|:--------|:-------------|:----:|
| v0.3.0  | (unreleased) | 8    |
| v0.2.0  | 2016-12-12   | 8    |
| v0.1.0  | 2015-12-21   | 7    |


### v0.3.0
*(unreleased)*
* runs on Java 8
* includes more primitive types:
  * Integer
  * List_Integer
  * Decimal
  * List_Decimal
  * Empty
* executes `normalize` extension, if no extension is given
* shows a warning instead of stopping when the normalization finds duplicated identifiers
* includes a `prefix` map to shorten URIs
* build command:

```
$ mvn clean install
```

* release: `tabula-distribution/target/tabula-0.3.0.jar`


### v0.2.0
*(2016-12-12)*
* runs on Java 8
* build command:

```
$ mvn clean install
```

* release: `tabula-distribution/target/tabula-0.2.0.jar`


### v0.1.0
*(2015-12-21)*
* runs on Java 7
* primitive types:
  * String
  * List_String
  * URI
  * List_URI
* readers (extension names between parentheses):
  * (`simple` / `normalize`) tabula format
  * (`parsecsv`) comma-separated values
  * (`parsecalendar`) calendar
* writers (extension names between parentheses):
  * (`simple` / `normalize`) tabula format
  * (`csv`) comma-separated values
  * (`html`) HTML
  * (`wikitext`) WikiText
  * (`sql`) SQL
* build command:

```
$ mvn clean install
```

* release: `tabula-distribution/target/tabula-0.1.0.jar`


