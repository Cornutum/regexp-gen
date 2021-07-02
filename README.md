# rexexp-gen

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.cornutum.regexp/regexp-gen/badge.svg?style=plastic)](https://maven-badges.herokuapp.com/maven-central/org.cornutum.regexp/regexp-gen)
[![javadoc](https://javadoc.io/badge2/org.cornutum.regexp/regexp-gen/javadoc.svg?style=plastic)](https://javadoc.io/doc/org.cornutum.regexp/regexp-gen)

## Contents ##

  * [What's New?](#whats-new)
  * [What Is It?](#what-is-it)
  * [How Does It Work?](#how-does-it-work)
    * [The basics](#the-basics)
    * [Exact matches vs. substring matches](#exact-matches-vs-substring-matches)
    * [What matches "dot"?](#what-matches-dot)
    * [How to create longer matching strings](#how-to-create-longer-matching-strings)
    * [How to generate random matches repeatably](#how-to-generate-random-matches-repeatably)
    * [How to limit match length to a specific range](#how-to-limit-match-length-to-a-specific-range)
  * [FAQs](#faqs)

## What's New? ##

  * The latest version ([1.2.3](https://github.com/Cornutum/regexp-gen/releases/tag/release-1.2.3))
    is now available at the [Maven Central Repository](https://search.maven.org/search?q=regexp-gen).

## What Is It? ##

`regexp-gen` is a Java library that implements the `RegExpGen` interface. A `RegExpGen` generates
random strings that match a specified [regular expression](https://www.rexegg.com/). There could be
many applications for this capability, but it's especially useful for testing a system that uses
regular expressions to process its inputs.

## How Does It Work? ##

### The basics ###

Here's a simple example of how to use a `RegExpGen`. 

```java
import org.cornutum.regexpgen.RandomGen;
import org.cornutum.regexpgen.RegExpGen;
import org.cornutum.regexpgen.js.Parser;
import org.cornutum.regexpgen.random.RandomBoundsGen;

// Given a JavaScript regular expression...
String regexp = "^Regular expressions are ((odd|hard|stupid), )+but cool!$";

// ...and a random number generator...
RandomGen random = new RandomBoundsGen();

// ...create a RegExpGen instance...
RegExpGen generator = Parser.parseRegExp( regexp);

for( int i = 0; i < 3; i++)
  {
  // ... and generate matching strings
  System.out.println( generator.generate( random));
  }
```

Run this example and you'll see output something like this:

```
Regular expressions are hard, but cool!
Regular expressions are hard, odd, stupid, but cool!
Regular expressions are odd, odd, but cool!
```

### Exact matches vs. substring matches ###

What does it mean for a string to match a regular expression? In general, this means "contains at
least one substring that matches". For example, consider what happens when you run the following example.

```java
...
// Given a JavaScript regular expression...
String regexp = "(Hello|Howdy|Allô), world!";

// ...create a RegExpGen instance...
RegExpGen generator = Parser.parseRegExp( regexp);
...
```

In this case, you'll see something like this:

```
x(©yÚULD+|/7OoÁÇj%_]Hello, world!!U}oñ(eöÛ½fÕõ
ªâæÎø^üp{/Ã0ßiØ1·AÂø]ýF%hÅµ¦<vËÈàHowdy, world!»¿
l^È¨ú(¹C³TÂI6ÓQaª}f*Allô, world!p±!éÇ'>aDÙ
```

These strings contain not only a substring that matches the regular expression, but also some
[extraneous characters](#what-matches-dot) before and/or after the match. If the consumer of these strings recognizes a
general "substring match", that's exactly what you want to give it.

But what if you need to generate only "exact" matches? In other words, strings containing only the matching characters.
To do that, use `Parser.parseRegExpExact()`.

```java
...
// Given a JavaScript regular expression...
String regexp = "(Hello|Howdy|Allô), world!";

// ...create a RegExpGen instance...
RegExpGen generator = Parser.parseRegExpExact( regexp);
...
```

Run with this change and the result will look like this:

```
Hello, world!
Allô, world!
Howdy, world!
```

### What matches "dot"? ###

What matches the regular expression `.`? In general, any printable character. For a `RegExpGen`, by default, that means "any printable character
from the Latin-1 basic and supplemental Unicode blocks". But what if that includes characters that your application is not designed to handle?
You can define exactly what "any printable character" means using the `GenOptions` for a `RegExpGen`.

For example, you could make a ridiculously narrow definition like this:

```java
...
// Given a JavaScript regular expression...
String regexp = regexp( "<< My secret is [^\\d\\s]{8,32} >>");

// ...and a random number generator...
RandomGen random = getRandomGen();

// ...create a RegExpGen instance...
RegExpGen generator = Parser.parseRegExp( regexp);

// ...matching "." with specific characters...
generator.getOptions().setAnyPrintableChars( "1001 Anagrams!");
...
```

Run this example and the result will be something like this:

```
Aag<< My secret is sgnggag!amm! >>s!Ag
s11aa0Agra<< My secret is rsm!!nA!!Aanmnsgmmr >>m
0m1ra! !n1gr 1<< My secret is mangAmr!!!m >>
```

Notice how the implicit `.*` expressions that generate the beginning and ending of [substring
matches](#exact-matches-vs-substring-matches) draw only from the characters specified by
`GenOptions.setAnyPrintableChars()`. And notice something else: the definition of "any printable"
also affects how matches are generated for exclusionary character classes of the form `[^...]`.
That's because these classes are interpreted to mean "any printable char *except* for...".

For convenience, `GenOptions` defines some common candidates for "any printable", such as
`GenOptions.ANY_LATIN_1` and `GenOptions.ANY_ASCII`.

### How to create longer matching strings ###

For any regular expression, there is always a miniumum length for any matching string.  Sometimes
matches are also limited to some maximum length. `RegExpGen` generates matches of random length that
always lie within the minimum and maximum bounds for the regular expression.

But for many regular expressions, like the one in [the basic example above](#the-basics), there is no upper bound on
the length of a matching string. Nevertheless, as a practical matter, some kind of upper bound is
usually helpful.  What to do?

The `RandomBoundGen` class offers a way to control this. When choosing a length from an unbounded
range, `RandomBoundGen` picks a random value that exceeds the minimum by only a limited amount. It
uses a [Poisson distribution](https://en.wikipedia.org/wiki/Poisson_distribution) to ensure that
most lengths above the minimum are near a specified average value and rarely exceed the minimum
by very much more than that. By default, `RandomBoundsGen` will limit values to an average of 16
more than the minimum.

But what if you'd like to create longer matches? Here's how to change the `lambda` parameter of a
`RandomBoundsGen` to define a larger average increment above minimum:


```java
...
// ...and a random number generator that limits the length of unbounded matches to
// to an average of minimum + 64 ..
RandomGen random = new RandomBoundsGen( 64);
...
```
Run with this change and you'll see output something like this:

```
Regular expressions are odd, but cool!
Regular expressions are odd, odd, stupid, stupid, odd, but cool!
Regular expressions are odd, odd, odd, hard, odd, odd, odd, odd, stupid, but cool!
```

### How to generate random matches repeatably ###

If random strings generated by `RegExpGen` cause a failure in the system you're testing, you need to
isolate the cause of the failure and to verify a fix. To do so, you must be able to repeat the test
with the exact same results. But how?

To choose random values, `RandomBoundsGen` uses an instance of the standard Java
[`Random`](https://docs.oracle.com/javase/8/docs/api/java/util/Random.html) class. By default,
`RandomBoundsGen` creates a new `Random` instance with a random initial seed value, in which case
the sequence of values generated is *not* repeatable. But you can make it repeatable by specifying
your own seed value, like this:

```java
...
// ...and a random number generator with a specific seed value...
RandomGen random = new RandomBoundsGen( new Random( mySeed));
...
```

### How to limit match length to a specific range ###

Maybe you need to generate random match strings with a length that lies within a specific range. Is
that possible? Yes, albeit with certain caveats.

To limit the match length, you need to provide minimum and maximum values to the `RegExpGen`, like this:

```java
...
for( int i = 0; i < 3; i++)
  {
  // ... and generate matching strings with length between 42 (inclusive) and 52 (inclusive)
  System.out.println( generator.generate( random, 42, 52));
  }
```

Run with this change and you'll usually see output something like this, with all matches within the
specified limits.

```
Regular expressions are hard, odd, odd, but cool!
Regular expressions are odd, hard, hard, but cool!
Regular expressions are odd, odd, but cool!
```

But occasionally, you'll see something like the following. Oops! The last match is only 41
characters. What happened?

```
Regular expressions are hard, odd, stupid, but cool!
Regular expressions are hard, hard, but cool!
Regular expressions are stupid, but cool!
```

Because `RexExpGen` tries to find random matches within the limits, it will eventually try to find a
match with length 42. But, for this regular expression, no such match is possible! Every attempt
builds a match up to 41 characters and then hits a dead end.

Figuring out if a specific match length is impossible is a really hard problem. So `RegExpGen` can't guarantee that
matches will always lie between the given limits. Instead, `RegExpGen` makes a slightly different promise:

> Given a lower bound `L` and an upper bound `U`, the length of generated matches
> will always be <= `U` and will usually be >= `L` but sometimes may be slightly < `L`.


## FAQs ##

  * **How do I add this as a dependency to my Maven project?**

    Add this to your project POM:

    ```xml
    <dependency>
      <groupId>org.cornutum.regexp</groupId>
      <artifactId>regexp-gen</artifactId>
      <version>...</version>
    </dependency>
    ```

  * **How can I run some examples?**

    Try running the [ExampleTest](https://github.com/Cornutum/regexp-gen/blob/master/src/test/java/org/cornutum/regexpgen/examples/ExampleTest.java).

  * **I'm getting strings with lots of crazy extra characters. What's the deal?**

    You should read about the difference between ["exact" matches and "substring" matches](#exact-matches-vs-substring-matches).
    And if that's too crazy for you, try changing the definition of [what matches "dot"](#what-matches-dot).
    
  * **Where's the Javadoc?**

    For full details, see the complete Javadoc [here](http://www.cornutum.org/regexp-gen/apidocs/).

  * **What flavor of regular expression syntax is supported?**

    The `RegExpGen` interface is designed to allow implementations for different regular expression
    engines. The current version provides an implementation for JavaScript `RegExp` (specifically,
    as defined by the [ECMAScript](https://www.ecma-international.org/ecma-262/#sec-patterns)
    standard).

    Note that the syntax for the Java `Pattern` implementation overlaps quite a bit with `RegExp`,
    so this `RegExpGen` implementation can also be used to generate matches for most Java `Pattern`
    instances.

  * **Are all possible regular expressions accepted?**

    Mostly, but there are a few exceptions.

    * Negative look-behind (`(?<!...)`) and negative look-ahead assertions (`(?!...)`) are not supported.

    * Word boundary assertions (`\b` and `\B`) are not supported.

    * Sequences of multiple consecutive look-ahead/behind assertions are not supported.

    * Back references to capturing groups are not supported. No distinction is made between capturing
      and non-capturing groups, and group names are ignored.

  * **Now that I've created a `RegExpGen`, how do I know the regular expression it's generating matches for?**

    Easy -- just call `RegExpGen.getOptions().getRegExp()`.

  * **Hey, what happened to release version 1.2.2?**

    Due to a deployment error, version 1.2.2 is defunct, deprecated, and definitely non-grata. Although it
    cannot be removed from the Maven Central repository, you should NOT use that version.
