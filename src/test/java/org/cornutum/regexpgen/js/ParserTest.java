//////////////////////////////////////////////////////////////////////////////
// 
//                    Copyright 2020, Cornutum Project
//                             www.cornutum.org
//
//////////////////////////////////////////////////////////////////////////////

package org.cornutum.regexpgen.js;

import org.cornutum.regexpgen.RegExpGen;

import org.junit.Test;
import static org.cornutum.hamcrest.Composites.*;
import static org.hamcrest.MatcherAssert.*;

/**
 * Runs tests for {@link Parser}.
 */
public class ParserTest
  {
  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 0. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> \ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> d </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> t </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> ? </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_0()
    {
    // Given...
    String regexp = "^\\((Copyright[-: ]+2020)?[\\\\\\d\\t]? K(?=ornutum)\\)|@Copyright|@Trademark";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add( "(")
        .add(
          SeqGen.builder()
          .add( "Copyright")
          .add( AnyOfGen.builder().addAll( "-: ").occurs( 1, null).build())
          .add( "2020")
          .occurs( 0, 1)
          .build())
        .add(
          AnyOfGen.builder()
          .add( '\\')
          .addAll( CharClassGen.digit())
          .add( '\t')
          .occurs( 0, 1)
          .build())
        .add( " ")
        .add( "K")
        .add( SeqGen.builder().add( "ornutum").build())
        .add(
          SeqGen.builder()
          .add( ")")
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build(),

        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( "@")
          .build())
        .add( "Copyrigh")
        .add(
          SeqGen.builder()
          .add( "t")
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build(),

        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( "@")
          .build())
        .add( "Trademar")
        .add(
          SeqGen.builder()
          .add( "k")
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build())
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 1. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ( </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> * </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_1()
    {
    // Given...
    String regexp = "-+|\\([^\\0-]*?\\0\\)|-";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add(
            AnyOfGen.builder().anyPrintable().occurs( 0, null).build(),
            AnyOfGen.builder().add( '-').occurs( 1, null).build())
          .build())
        .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
        .build())

      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( "(")
          .build())
        .add( NoneOfGen.builder().add( (char) 0).add( '-').occurs(0).build())
        .add( AnyOfGen.builder().add( (char) 0).occurs( 1).build())
        .add(
          SeqGen.builder()
          .add( ")")
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build())

      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( AnyOfGen.builder().add( '-').occurs( 1).build())
          .build(),
          AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
        .build())
      
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 2. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> c </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> + </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_2()
    {
    // Given...
    String regexp = "(?<=[\\ca-\\ck]+)\\cZ+$";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      SeqGen.builder()
      .add(
        SeqGen.builder()
        .add(
          AnyOfGen.builder().anyPrintable().occurs( 0, null).build(),
          AnyOfGen.builder().addAll( (char) 0x0001, (char) 0x000B).occurs( 1, null).build())
        .build(),
        AnyOfGen.builder().add( (char) 0x001A).occurs( 1, null).build())
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 3. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> [ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> s </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> u </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> \ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> n </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> 2 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_3()
    {
    // Given...
    String regexp = "(?<=Prefix)([^\\sA-Z\\uABCD@]\\n)+|X\\\\=.{0,2}(?=Suffix)";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add(
            SeqGen.builder()
            .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
            .add( "P")
            .build())
          .add( "refix")
          .build())
        .add(
          SeqGen.builder()
          .add(
            SeqGen.builder()
            .add(
              NoneOfGen.builder()
              .addAll( CharClassGen.space())
              .addAll( 'A', 'Z')
              .add( (char) 0xABCD)
              .add( '@')
              .build())
            .add( AnyOfGen.builder().add( '\n').build())
            .occurs( 1, null)
            .build())
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build())

      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( "X")
          .build())
        .add( "\\=")
        .add( AnyOfGen.builder().anyPrintable().occurs( 0, 2).build())
        .add(
          SeqGen.builder()
          .add( "Suffi")
          .add(
            SeqGen.builder()
            .add( "x")
            .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
            .build())
          .build())
        .build())
      
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 4. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> b </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> f </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> + </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_4()
    {
    // Given...
    String regexp = "^[\\b]\\\\+?\\f$";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      SeqGen.builder()
      .add(
        AnyOfGen.builder().add( '\b').build(),
        AnyOfGen.builder().add( '\\').occurs(1).build(),
        AnyOfGen.builder().add( '\f').build())
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 5. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> r </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> ? </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_5()
    {
    // Given...
    String regexp = "\\rX|^\\r??$|[^\\r]";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( "\r")
          .build())
        .add(
          SeqGen.builder()
          .add( "X")
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .build())

      .add( AnyOfGen.builder().add( '\r').occurs( 0).build())

      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( NoneOfGen.builder().add( '\r').build())
          .build())
        .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
        .build())
      
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 6. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> ] </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> S </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> v </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ( </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> x </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> None </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_6()
    {
    // Given...
    String regexp = "(?<=^Prefix)([\\S@X-Z\\]\\v]\\(\\x6A.z\\))*?$";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      SeqGen.builder()
      .add( SeqGen.builder().add( "Prefix").build())
      .add(
        SeqGen.builder()
        .add(
          AnyOfGen.builder()
          .addAll( CharClassGen.nonSpace())
          .add( '@')
          .addAll( 'X', 'Z')
          .add( ']')
          .add( (char) 0x000B)
          .build())
        .add( "(")
        .add( AnyOfGen.builder().add( (char) 0x006A).build())
        .add( AnyOfGen.builder().anyPrintable().build())
        .add( "z)")
        .occurs( 0)
        .build())
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 7. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> c </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> \ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> t </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> * </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_7()
    {
    // Given...
    String regexp = "(?<=^(?<group>A|B))([^\\cC]*\\\\\\t)+(?=Z$|D)|(Z|D$)";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add(
          AlternativeGen.builder()
          .add( AnyOfGen.builder().add( 'A').build())
          .add( AnyOfGen.builder().add( 'B').build())
          .build())
        .add(
          SeqGen.builder()
          .add( NoneOfGen.builder().add( (char) 0x0003).occurs( 0, null).build())
          .add( "\\\t")
          .occurs( 1, null)
          .build())
        .add(
          AlternativeGen.builder()
          .add( AnyOfGen.builder().add( 'Z').build())
          .add(
            SeqGen.builder()
            .add( AnyOfGen.builder().add( 'D').build())
            .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
            .build())
          .build())
        .build())
      
      .add(
        AlternativeGen.builder()
        .add(
          SeqGen.builder()
          .add(
            SeqGen.builder()
            .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
            .add( AnyOfGen.builder().add( 'Z').build())
            .build())
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .build())
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( AnyOfGen.builder().add( 'D').build())
          .build())
        .build())
      
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 8. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> u </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 2 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_8()
    {
    // Given...
    String regexp = "\\u028f{2,3}$";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      SeqGen.builder()
      .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
      .add(
        AnyOfGen.builder()
        .add( (char) 0x028F)
        .occurs( 2, 3)
        .build())
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 9. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> \ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> W </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> f </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> v </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 2 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> 2 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_9()
    {
    // Given...
    String regexp = "\\?|(?:^[^\\\\-z\\W\\f-]\\)\\v+.){2,2}?(?=Suffix)";
    
    // When...
    RegExpGen generator = Parser.parseRegExp( regexp);

    // Then...
    RegExpGen expected =
      AlternativeGen.builder()
      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
          .add( AnyOfGen.builder().add( '?').build())
          .build())
        .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
        .build())

      .add(
        SeqGen.builder()
        .add(
          SeqGen.builder()
          .add(
            NoneOfGen.builder()
            .addAll( '\\', 'z')
            .addAll( CharClassGen.nonWord())
            .add( '\f')
            .add( '-')
            .build())
          .add( ")")
          .add( AnyOfGen.builder().add( (char) 0x000b).occurs( 1, null).build())
          .add( AnyOfGen.builder().anyPrintable().build())
          .occurs( 2)
          .build())
        .add(
          SeqGen.builder()
          .add( "Suffi")
          .add(
            SeqGen.builder()
            .add( AnyOfGen.builder().add( 'x').build())
            .add( AnyOfGen.builder().anyPrintable().occurs( 0, null).build())
            .build())
            .build())
        .build())
      
      .build();

    assertThat( generator, matches( new RegExpGenMatcher( expected)));
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 10. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> n </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ( </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 2 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> None </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_10()
    {
    // properties = alternative,bounded,charClass,lookbehind,quantifier,term

    // Given...
    //
    //   Anchor-Start = No
    //
    //   Alternatives.Count = 1
    //
    //   Alternatives.Terms.Count = 3
    //
    //   Alternatives.Terms.Look-Behind = Yes
    //
    //   Alternatives.Terms.Group = No
    //
    //   Alternatives.Terms.Char-Class.Count = 1
    //
    //   Alternatives.Terms.Char-Class.Excluded = No
    //
    //   Alternatives.Terms.Char-Class.Range = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = n
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = No
    //
    //   Alternatives.Terms.Atom-Escape.Literal = (
    //
    //   Alternatives.Terms.Atom-Escape.Char = 0
    //
    //   Alternatives.Terms.Dot = No
    //
    //   Alternatives.Terms.Pattern-Char = No
    //
    //   Alternatives.Terms.Look-Ahead = No
    //
    //   Alternatives.Terms.Quantifier.Type = Bounded
    //
    //   Alternatives.Terms.Quantifier.Min = 2
    //
    //   Alternatives.Terms.Quantifier.Max = None
    //
    //   Alternatives.Terms.Quantifier.Lazy = No
    //
    //   Anchor-End = Yes
    
    // When...

    // Then...
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 11. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> c </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> Bounded </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> 0 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_11()
    {
    // properties = alternative,bounded,lookahead,lookbehind,quantifier,term

    // Given...
    //
    //   Anchor-Start = No
    //
    //   Alternatives.Count = Many
    //
    //   Alternatives.Terms.Count = 1
    //
    //   Alternatives.Terms.Look-Behind = Yes
    //
    //   Alternatives.Terms.Group = No
    //
    //   Alternatives.Terms.Char-Class.Count = 0
    //
    //   Alternatives.Terms.Char-Class.Excluded = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Range = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = No
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = No
    //
    //   Alternatives.Terms.Atom-Escape.Literal = No
    //
    //   Alternatives.Terms.Atom-Escape.Char = c
    //
    //   Alternatives.Terms.Dot = No
    //
    //   Alternatives.Terms.Pattern-Char = No
    //
    //   Alternatives.Terms.Look-Ahead = Yes
    //
    //   Alternatives.Terms.Quantifier.Type = Bounded
    //
    //   Alternatives.Terms.Quantifier.Min = 0
    //
    //   Alternatives.Terms.Quantifier.Max = 3
    //
    //   Alternatives.Terms.Quantifier.Lazy = Yes
    //
    //   Anchor-End = No
    
    // When...

    // Then...
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 12. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> [ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> D </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> r </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> \ </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> ? </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_12()
    {
    // properties = alternative,charClass,quantifier,term

    // Given...
    //
    //   Anchor-Start = Yes
    //
    //   Alternatives.Count = 1
    //
    //   Alternatives.Terms.Count = Many
    //
    //   Alternatives.Terms.Look-Behind = No
    //
    //   Alternatives.Terms.Group = Yes
    //
    //   Alternatives.Terms.Char-Class.Count = Many
    //
    //   Alternatives.Terms.Char-Class.Excluded = No
    //
    //   Alternatives.Terms.Char-Class.Range = Yes
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = [
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = D
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = r
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = Yes
    //
    //   Alternatives.Terms.Atom-Escape.Literal = \
    //
    //   Alternatives.Terms.Atom-Escape.Char = No
    //
    //   Alternatives.Terms.Dot = Yes
    //
    //   Alternatives.Terms.Pattern-Char = Yes
    //
    //   Alternatives.Terms.Look-Ahead = No
    //
    //   Alternatives.Terms.Quantifier.Type = ?
    //
    //   Alternatives.Terms.Quantifier.Min = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Max = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Lazy = No
    //
    //   Anchor-End = Yes
    
    // When...

    // Then...
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 13. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 3 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> x </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> ) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> n </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> * </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_13()
    {
    // properties = alternative,charClass,lookahead,quantifier,term

    // Given...
    //
    //   Anchor-Start = Yes
    //
    //   Alternatives.Count = Many
    //
    //   Alternatives.Terms.Count = 3
    //
    //   Alternatives.Terms.Look-Behind = No
    //
    //   Alternatives.Terms.Group = No
    //
    //   Alternatives.Terms.Char-Class.Count = 1
    //
    //   Alternatives.Terms.Char-Class.Excluded = Yes
    //
    //   Alternatives.Terms.Char-Class.Range = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = No
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = x
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = No
    //
    //   Alternatives.Terms.Atom-Escape.Literal = )
    //
    //   Alternatives.Terms.Atom-Escape.Char = n
    //
    //   Alternatives.Terms.Dot = No
    //
    //   Alternatives.Terms.Pattern-Char = No
    //
    //   Alternatives.Terms.Look-Ahead = Yes
    //
    //   Alternatives.Terms.Quantifier.Type = *
    //
    //   Alternatives.Terms.Quantifier.Min = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Max = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Lazy = Yes
    //
    //   Anchor-End = No
    
    // When...

    // Then...
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 14. parse (Success) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> 1 </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> Many </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> ] </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> w </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> t </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> Yes </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> + </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> No </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> Yes </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_14()
    {
    // properties = alternative,charClass,lookbehind,quantifier,term

    // Given...
    //
    //   Anchor-Start = No
    //
    //   Alternatives.Count = 1
    //
    //   Alternatives.Terms.Count = 1
    //
    //   Alternatives.Terms.Look-Behind = Yes
    //
    //   Alternatives.Terms.Group = No
    //
    //   Alternatives.Terms.Char-Class.Count = Many
    //
    //   Alternatives.Terms.Char-Class.Excluded = No
    //
    //   Alternatives.Terms.Char-Class.Range = Yes
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = ]
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = w
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = t
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = Yes
    //
    //   Alternatives.Terms.Atom-Escape.Literal = No
    //
    //   Alternatives.Terms.Atom-Escape.Char = No
    //
    //   Alternatives.Terms.Dot = No
    //
    //   Alternatives.Terms.Pattern-Char = No
    //
    //   Alternatives.Terms.Look-Ahead = No
    //
    //   Alternatives.Terms.Quantifier.Type = +
    //
    //   Alternatives.Terms.Quantifier.Min = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Max = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Lazy = No
    //
    //   Anchor-End = Yes
    
    // When...

    // Then...
    }

  /**
   * Tests {@link Parser#parse parse()} using the following inputs.
   * <P>
   * <TABLE border="1" cellpadding="8">
   * <TR align="left"><TH colspan=2> 15. parse (<FONT color="red">Failure</FONT>) </TH></TR>
   * <TR align="left"><TH> Input Choice </TH> <TH> Value </TH></TR>
   * <TR><TD> Anchor-Start </TD> <TD> No </TD> </TR>
   * <TR><TD> Alternatives.Count </TD> <TD> <FONT color="red"> 0  </FONT> </TD> </TR>
   * <TR><TD> Alternatives.Terms.Count </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Behind </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Group </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Count </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Excluded </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Range </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Literal </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Class </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Escape.Char </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Char-Class.Pattern-Char </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Literal </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Atom-Escape.Char </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Dot </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Pattern-Char </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Look-Ahead </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Type </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Min </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Max </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Alternatives.Terms.Quantifier.Lazy </TD> <TD> (not applicable) </TD> </TR>
   * <TR><TD> Anchor-End </TD> <TD> No </TD> </TR>
   * </TABLE>
   * </P>
   */
  @Test
  public void parse_15()
    {
    // Given...
    //
    //   Anchor-Start = No
    //
    //   Alternatives.Count = 0
    //
    //   Alternatives.Terms.Count = (not applicable)
    //
    //   Alternatives.Terms.Look-Behind = (not applicable)
    //
    //   Alternatives.Terms.Group = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Count = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Excluded = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Range = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Escape.Literal = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Escape.Class = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Escape.Char = (not applicable)
    //
    //   Alternatives.Terms.Char-Class.Pattern-Char = (not applicable)
    //
    //   Alternatives.Terms.Atom-Escape.Literal = (not applicable)
    //
    //   Alternatives.Terms.Atom-Escape.Char = (not applicable)
    //
    //   Alternatives.Terms.Dot = (not applicable)
    //
    //   Alternatives.Terms.Pattern-Char = (not applicable)
    //
    //   Alternatives.Terms.Look-Ahead = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Type = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Min = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Max = (not applicable)
    //
    //   Alternatives.Terms.Quantifier.Lazy = (not applicable)
    //
    //   Anchor-End = No
    
    // When...

    // Then...
    }
  }
