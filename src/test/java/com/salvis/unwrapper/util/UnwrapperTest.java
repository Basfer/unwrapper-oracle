package com.salvis.unwrapper.util;


import org.junit.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class UnwrapperTest {

    @Test
    public void unwrap() throws NoSuchAlgorithmException, IOException, DataFormatException {
        final String INPUT = "STUFF_TO_BE_IGNORED\n" +
                "create or replace PROCEDURE sample_procedure wrapped\n" +
                "a000000\n" +
                "1\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "7\n" +
                "75 a6\n" +
                "FSfycu7ZcUm1cP88FuVQDiSeSqgwg5nnm7+fMr2ywFwW3EdihaHwlhaXrtwuPmLyXKV0i8DA\n" +
                "Mv7ShglpaedKdDzHUpuySv4osr3nsrMdBjAsriTqsjJ0eSaUJvpjbCY+I/zsPHHiP9Ger0q8\n" +
                "+yLiCPt8E9iIpqeNFPQ=\n";

        final String OUTPUT = "STUFF_TO_BE_IGNORED\n" +
                "create or replace PROCEDURE sample_procedure wrapped\n" +
                "PROCEDURE sample_procedure IS\n" +
                "BEGIN\n" +
                "   SYS.DBMS_OUTPUT.PUT_LINE('sample_procedure executed.');\n" +
                "END SAMPLE_PROCEDURE;";

        assertEquals(OUTPUT, Unwrapper.unwrap(INPUT));
    }

    @Test
    public void unwrapPackageWithSpecAndBody() throws NoSuchAlgorithmException, IOException, DataFormatException {
        final String INPUT = "CREATE OR REPLACE package post_util wrapped\n" +
                "a000000\n" +
                "98\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "9\n" +
                "98 be\n" +
                "YmfXsBecM76T5Du66XyQIXdLNJEwg3kYLcsVfHREk5BkpefTtvawdyp5Ni5mxsnXX8O9FIO1\n" +
                "lunj9BIFVdlB0bwXzyKHx0yL+EZrU7K0n2FaqwnbQQ1K2IT8M8umA0msEx+rNG6PEEYLSNly\n" +
                "j5zQSbck7qkVJ9N/g7TCIUoLB62UO+jug4or4LNmjg==\n" +
                "\n" +
                "/\n" +
                "CREATE OR REPLACE package body post_util is\n" +
                "vMethod\tnumber;\n" +
                "\n" +
                "begin\n" +
                "\tu_util.g_u('METHOD', vMethod);\n" +
                "\tif vMethod is null then vMethod := 0; end if;\n" +
                "end post_util;\n" +
                "/\n";

        final String result = Unwrapper.unwrap(INPUT);

        assertTrue(result.contains("package body post_util"));
        assertTrue(result.contains("vMethod"));
        assertTrue(result.contains("u_util.g_u"));
    }

    @Test
    public void unwrapMultipleEncodedBlocks() throws NoSuchAlgorithmException, IOException, DataFormatException {
        final String INPUT = "STUFF_BEFORE\n" +
                "create or replace PROCEDURE proc1 wrapped\n" +
                "a000000\n" +
                "1\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "7\n" +
                "75 b3\n" +
                "FSfycu7ZcUm1cP88FuVQDiSeSqgwg5nnm7+fMr2ywFwW3EdihaHwlhaXrtwuPmLyXKV0i8DA\n" +
                "Mv7ShglpaedKdDzHUpuySv4osr3nsrMdBjAsriTqsjJ0eSaUJvpjbCY+I/zsPHHiP9Ger0q8\n" +
                "+yLiCPt8E9iIpqeNFPQ=\n" +
                "\n" +
                "/\n" +
                "create or replace PROCEDURE proc2 wrapped\n" +
                "a000000\n" +
                "1\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "abcd\n" +
                "7\n" +
                "75 a6\n" +
                "FSfycu7ZcUm1cP88FuVQDiSeSqgwg5nnm7+fMr2ywFwW3EdihaHwlhaXrtwuPmLyXKV0i8DA\n" +
                "Mv7ShglpaedKdDzHUpuySv4osr3nsrMdBjAsriTqsjJ0eSaUJvpjbCY+I/zsPHHiP9Ger0q8\n" +
                "+yLiCPt8E9iIpqeNFPQ=\n" +
                "\n" +
                "STUFF_AFTER";

        final String result = Unwrapper.unwrap(INPUT);

        assertTrue("result contains wrapped keyword", result.contains("wrapped"));
        assertTrue("result contains decoded content", result.contains("PROCEDURE sample_procedure IS"));
        assertTrue("result contains STUFF_BEFORE", result.contains("STUFF_BEFORE"));
        assertTrue("result contains STUFF_AFTER", result.contains("STUFF_AFTER"));
        assertTrue("result contains separator", result.contains("/"));
    }
}
