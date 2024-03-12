package internal;

import com.intuit.karate.junit5.Karate;

class InternalRunner {
    
    @Karate.Test
    Karate testUsers() {
        return Karate.run("users").relativeTo(getClass());
    }    
    
    @Karate.Test
    Karate testLista() {
        return Karate.run("principal").relativeTo(getClass());
    }      
}
