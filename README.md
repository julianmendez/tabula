# Tabula
*System to manage human-readable tables using files*


[![Build Status](https://travis-ci.org/julianmendez/tabula.png?branch=master)](https://travis-ci.org/julianmendez/tabula)


Tabula is a system to manage human-readable tables using files. It uses a specific type of file format that is similar to a Java .properties, but allows defining the same property for different objects.


## Source code

To clone and compile the project:

```
$ git clone https://github.com/julianmendez/tabula.git
$ cd tabula
$ mvn clean install
```


## Author
[Julian Mendez](http://lat.inf.tu-dresden.de/~mendez/)


## License

This software is distributed under the [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).


## Format

The Tabula format has *primitive types* and *composite types*. Unless something different is stated in the [release notes](http://github.com/julianmendez/tabula/blob/master/RELEASE-NOTES.md), the primitive types are:
* `String`: any string without any newline ('\n' 0x0A, '\r' 0x0D), and not ending in backslash ('\' 0x5C), neither in blanks ('\t' 0x08, ' ' 0x20)  
* `URI`: any valid Uniform Resource Identifier
* `List_String`: list of space-separated words
* `List_URI`: list of space-separated URIs

With this format it is possible to define one or many composite *types*. Each type is defined by its *fields*. The *instances* of each type are declared just after the type definition.
The name of a type or field can be any *identifier*. A identifier is a word that is not any of the reserved words: `type`, `def`, `new`, `id`.
Instances can be identified by the field `id`.

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


## Example

```properties
# simple format 1.0.0


type = record 


def = \
 id:String \
 title:String \
 authors:List_String \
 web:URI \
 documents:List_URI


order = \
 id


new = 
id = arXiv:1412.2223
title = A topological approach to non-Archimedean Mathematics
authors = \
 Vieri_Benci \
 Lorenzo_Luperi_Baglini
web = http://arxiv.org/abs/1412.2223
documents = \
 http://arxiv.org/pdf/1412.2223#pdf \
 http://arxiv.org/ps/1412.2223#ps \
 http://arxiv.org/format/1412.2223#other


new = 
id = arXiv:1412.3313
title = Infinitary stability theory
authors = \
 Sebastien_Vasey
web = http://arxiv.org/abs/1412.3313
documents = \
 http://arxiv.org/pdf/1412.3313#pdf \
 http://arxiv.org/ps/1412.3313#ps \
 http://arxiv.org/format/1412.3313#other


```
