# [Tabula](https://julianmendez.github.io/tabula/)

[![build](https://github.com/julianmendez/tabula/workflows/Java%20CI/badge.svg)](https://github.com/julianmendez/tabula/actions)
[![maven central](https://maven-badges.herokuapp.com/maven-central/de.tu-dresden.inf.lat.tabula/tabula-parent/badge.svg)](https://search.maven.org/#search|ga|1|g%3A%22de.tu-dresden.inf.lat.tabula%22)
[![license](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.txt)

*System to manage human-readable tables using files*

Tabula is a system to manage human-readable tables using files. It uses a specific type of file format that is similar to a [Java Properties](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html#load-java.io.Reader-) file, but allows defining the same property for different objects. The most updated specification can be found in the Scala implementation: [Tabulas](https://github.com/julianmendez/tabulas).


## Download

* [executable JAR file](https://sourceforge.net/projects/latitude/files/tabula/0.2.0/tabula-0.2.0.jar/download)
* [The Central Repository](https://repo1.maven.org/maven2/de/tu-dresden/inf/lat/tabula/)
* as dependency:

```xml
<dependency>
  <groupId>de.tu-dresden.inf.lat.tabula</groupId>
  <artifactId>tabula-ext</artifactId>
  <version>0.2.0</version>
</dependency>
```


## Format

The Tabula format has *primitive types* and *composite types*. Unless something different is stated in the [release notes](https://github.com/julianmendez/tabula/blob/master/RELEASE-NOTES.md), the primitive types are:

* `String`: any string without any newline (`'\n'` 0x0A, `'\r'` 0x0D), and not ending in backslash (`'\'` 0x5C), neither in blanks (`'\t'` 0x08, `' '` 0x20)
* `URI`: any valid Uniform Resource Identifier
* `Integer`: an integer number (`BigInteger`)
* `Decimal`: a decimal number (`BigDecimal`)
* `List_`... (e.g. `List_String`): list of space-separated values, for the types above
* `Empty`: type that ignores any given value

With this format it is possible to define one or many composite *types*. Each type is defined by its *fields*. The *instances* of each type are listed just after the type definition. The type name can be any Tabula String.
The field name can be any Tabula String that does not contain an equals sign (`'='` 0x3D), and that is not the words `type` or `new`. The field name `id` is reserved to identify instances. Thus, two instances of the same type cannot have the same identifier.

Each type is defined as follows:

```properties
type = TYPE_NAME
```

where *TYPE_NAME* can be any identifier.

Each type has its *fields*, defined as follow:

```properties
def = \
 FIELD_NAME_0:FIELD_TYPE_0 \
 FIELD_NAME_1:FIELD_TYPE_1 \
...
 FIELD_NAME_n:FIELD_TYPE_n
```

where each *FIELD_NAME* can be any identifier,
and each *FIELD_TYPE* can be any of the primitive types.

The URIs can be shortened by using prefixes. The prefixes are URIs themselves without colons, because the colon (`:`) is used to define the association.

```properties
prefix = \
 PREFIX_0:URI_0 \
 PREFIX_1:URI_1 \
 ...
 PREFIX_n:URI_n
```

They are applied using the declaration order during parsing and serialization. Although the serialization shortens every possible URI using the prefixes, it is possible to expand all of them by adding the empty prefix with an empty value, i.e. a colon (`:`) alone, and it has to be the first prefix. This could be useful to rename the prefixes.

The order in which the instances are shown is defined as follows:

```properties
order = \
 [-]FIELD_NAME_a_0 \
 [-]FIELD_NAME_a_1 \
 ...
 [-]FIELD_NAME_a_k
```

where the `-` is optional and used to denote reverse order. For example:

```properties
order = \
 id \
 -author
```

orders the instances by `id` (ascending) and then by author (descending).

The instances come just after the type definition, with the following syntax:

```properties
new =
FIELD_NAME_0 = VALUE_0
FIELD_NAME_1 = VALUE_1
...
FIELD_NAME_n = VALUE_n
```

where each *FIELD_NAME* is one of the already declared field names in the type and each *VALUE* contains a String accoding to the field type.

The *values* can be any Tabula String. The blanks (`'\t'` 0x08, `' '` 0x20) at the beginning and at the end are removed. To declare a multi-line value, each line must finish with backslash (`'\'` 0x5C), except the last one. For the sake of simplicity there is no difference between a multi-line value or the concatenation of all those lines. This means that:

```properties
field_name = \
 a \
 b \
 c
```

is the same as

```properties
field_name = a b c
```

However, the format will normalize and present them differently according to the declared type. Thus, the values of fields with type `List_`... (e.g. `List_String`) will be presented as multi-line values.


## Example

This is an example of a library file. Each book record contains an identifier (`id`), a title (`title`), the authors (`authors`), a link to the abstract on the web (`web`), and a list of links to the documents (`documents`). This file is ordered by identifier.

```properties


# simple format 1.0.0

type = record

def = \
 id:String \
 title:String \
 authors:List_String \
 web:URI \
 documents:List_URI

prefix = \
 arxiv:https://arxiv.org/

order = \
 id

new =
id = arXiv:1412.2223
title = A topological approach to non-Archimedean Mathematics
authors = \
 Vieri_Benci \
 Lorenzo_Luperi_Baglini
web = https://arxiv.org/abs/1412.2223
documents = \
 https://arxiv.org/pdf/1412.2223#pdf \
 https://arxiv.org/ps/1412.2223#ps \
 https://arxiv.org/format/1412.2223#other

new =
id = arXiv:1412.3313
title = Infinitary stability theory
authors = \
 Sebastien_Vasey
web = &arxiv;abs/1412.3313
documents = \
 &arxiv;pdf/1412.3313#pdf \
 &arxiv;ps/1412.3313#ps \
 &arxiv;format/1412.3313#other

```

An example like this one is used for the unit tests.

For example, the [MainTest](https://github.com/julianmendez/tabula/blob/master/tabula-core/src/test/java/de/tudresden/inf/lat/tabula/main/MainTest.java) class does the following steps:

* read the [example file](https://github.com/julianmendez/tabula/blob/master/tabula-core/src/test/resources/example.properties)
* add a new field `numberOfAuthors`
* add to each record the number of authors
* compare the [expected result](https://github.com/julianmendez/tabula/blob/master/tabula-core/src/test/resources/example-modified.properties)

This [Bash script](https://github.com/julianmendez/tabula/blob/master/docs/examples/tabula.sh.txt) shows how to start Tabula from the command line.


## Source code

To clone and compile the project:

```
$ git clone https://github.com/julianmendez/tabula.git
$ cd tabula
$ mvn clean install
```

The created executable library, its sources, and its Javadoc will be in `tabula-distribution/target`.

To compile the project offline, first download the dependencies:

```
$ mvn dependency:go-offline
```

and once offline, use:

```
$ mvn --offline clean install
```

The bundles uploaded to [Sonatype](https://oss.sonatype.org/) are created with:

```
$ mvn clean install -DperformRelease=true
```

and then on each module:

```
$ cd target
$ jar -cf bundle.jar tabula-*
```

and on the main directory:

```
$ cd target
$ jar -cf bundle.jar tabula-parent-*
```

The version number is updated with:

```
$ mvn versions:set -DnewVersion=NEW_VERSION
```

where *NEW_VERSION* is the new version.


## Author

[Julian Mendez](https://julianmendez.github.io)


## License

This software is distributed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).


## Release notes

See [release notes](https://julianmendez.github.io/tabula/RELEASE-NOTES.html).


## Contact

In case you need more information, please contact @julianmendez .


