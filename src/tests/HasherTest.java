package tests;

import bankapp.Hasher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HasherTest {

    @Test
    void hashingTest() {
        try {
            String[] tabela_wejsc = new String[]{"abba", "test", "", "abbs"};
            String[] tabela_wyjsc = new String[]
                    {"e22115b5d76640e2389bcac25c46a2df03b2df657c5b3ff32cbaed141f8e8ef0",
                     "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08",
                     "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                     "2be8549ff775f394898ce8a89221562ffa16b14237b45f7e444cf75fd29eb3bc"};
            for (int i = 0; i < tabela_wejsc.length; i++) {
                assertEquals(tabela_wyjsc[i],Hasher.sha256(tabela_wejsc[i]) );
            }
        }catch (Exception e){
            fail(); }
    }
}