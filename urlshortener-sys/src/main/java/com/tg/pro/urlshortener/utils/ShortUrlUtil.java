package com.tg.pro.urlshortener.utils;

import org.springframework.stereotype.Component;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ShortUrlUtil {

    private static final List<Character> BASE64_CHARS = Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '_');


    public String generate(int size,String charset)  {
        //get random string
        final Random rand = ThreadLocalRandom.current();
        byte[] b = new byte[size];
        char[] chars  = new char[size];
        for(int i= 0 ;i < size ;i++){
            b[i] = (byte) rand.nextInt(BASE64_CHARS.size());
            chars[i] =  BASE64_CHARS.get(b[i]);
        }

        //set charset
        Charset cs = Charset.forName (charset.toUpperCase());
        CharBuffer charBuffer = CharBuffer.allocate (chars.length);
        charBuffer.put (chars);
        charBuffer.flip ();
        ByteBuffer bb = cs.encode (charBuffer);
        String result = new String(bb.array());

        return result;
    }

}
