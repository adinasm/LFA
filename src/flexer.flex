import java.util.*;

%%

%class Flexer
%unicode
/*%debug*/
%int
/*%line*/
/*%column*/

%{
    public DFA dfa = new DFA();

    private String lastSource;
%}


LineTerminator = \r|\n|\r\n
WS = {LineTerminator} | [ \t\f]
special = "`"|"-"|"="|"["|"]"|";"|"'"|"\\"|"."|"/"|"~"|"!"|"@"|"#"|"$"|"%"|"^"|"&"|"*"|"_"|"+"|":"|"\""|"|"|"<"|">"|"?"
Symbol = [:uppercase:] | [:lowercase:] | [:digit:] | {special}
Name = ([:uppercase:] | [:lowercase:] | [:digit:] | "_")+

%state STARTK ELEMK STOPK SEPKS STARTS INITS ELEMS STOPS SEPSD STARTD INITD ELEMD SRCT SEPSST SYMT SEPSDT DESTT ENDT ENDD STOPD SEPDS SS SEPSF STARTF INITF ELEMF STOPF ENDDFA FINAL

%state DFA STATES STSEP ALPHABET ALSEP DELTA DSEP START STASE STOP STOSE

%%
{WS}	{}
<YYINITIAL>"(" {
    yybegin(STARTK);
}

<STARTK> "{" {
    yybegin(ELEMK);
}

<ELEMK> {Name} {
    // Process state
	yybegin(STOPK);
}

<STOPK> {
    "}" {
        yybegin(SEPKS);
    }
    ","	{
        yybegin(ELEMK);
    }
}

<SEPKS> "," {
    yybegin(STARTS);
}

<STARTS> "{" {
    // Done K, starting Sigma
    yybegin(INITS);
}

<INITS> "}" {
    yybegin(SEPSD);
}

<ELEMS, INITS> {Symbol}	{
    // Process symbol
    yybegin(STOPS);
}

<STOPS> {
    ","	{
        yybegin(ELEMS);
    }
    "}" {
        yybegin(SEPSD);
    }
}

<SEPSD> "," {
    // Done Sigma, starting delta
    yybegin(STARTD);
}

<STARTD> "(" {
    yybegin(INITD);
}

<INITD> ")" {
    yybegin(SEPDS);
}

<ELEMD,INITD> "(" {
    yybegin(SRCT);
}

<SRCT> {Name} {
    // Process source
    lastSource = yytext();

    yybegin(SEPSST);
}


<SEPSST> "," {
    yybegin(SYMT);
}

<SYMT> {Symbol} {
    // Process symbol
    yybegin(SEPSDT);
}

<SEPSDT> "," {
    yybegin(DESTT);
}

<DESTT> {Name} {
    // Add the transition to the dfa
    dfa.addTransition(lastSource, yytext());

    yybegin(ENDT);
}

<ENDT> ")" {
    yybegin(STOPD);
}

<STOPD> {
    "," {
        yybegin(ELEMD);
    }
    ")" {
        yybegin(SEPDS);
    }
}

<SEPDS> "," {
    yybegin(SS);
}

<SS> {Name} {
    // Process start state
    dfa.setStartState(yytext());

    yybegin(SEPSF);
}

<SEPSF> "," {
    yybegin(STARTF);
}

<STARTF> "{" {
    yybegin(INITF);
}

<INITF> "}" {
    yybegin(ENDDFA);
}

<INITF,ELEMF> {Name} {
    // Process final state
    dfa.addFinalState(yytext());

    yybegin(STOPF);
}

<STOPF> {
    "," {
        yybegin(ELEMF);
    }
    "}" {
        yybegin(ENDDFA);
    }
}
<ENDDFA> ")" {
    return 0;
}

. {
    System.err.println("Syntax error");
    System.exit(0);
}
