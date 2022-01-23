package br.com.example.pedro.businesscard.utils

class Verify {

    // Verified if all objects are empty
    fun isAllNotEmpty(list: List<String>):Boolean{

        val iterator = list.iterator()

        while (iterator.hasNext()){
            if (iterator.next().isEmpty())
                return false
        }

        return true;
    }

    fun verifyPhoneNumber(s: String):Boolean {

        // Check if it is empty
        if (s.isEmpty())
            return false

        // Checks if it has less than 9 characteres
        if (!(s.length >= 9))
            return false

        return true
    }

    fun verifyEmail(s: String):Boolean {
        // Verifies if it is empty
        if (s.isEmpty())
            return false

        // Verifies if it contais @
        if (! s.contains('@'))
            return false

        //Verifies the local term
        val local:String = s.substringBefore('@')
        val domain:String = s.substringAfter('@')

        if (!isLocalAddressEmailValid(local)){
            return false
        }

        if (!isDomainValid(domain)){
            return false
        }

        return true
    }

    private fun isDomainValid(domain: String): Boolean {
        // Verify if domain starts with hifen
        if (domain[0] == '-')
            return false

        var counter = 0;
        for (s in domain){
            if (s in '0'..'9'){
                counter += 1
            }
        }

        if (counter == domain.length){
            return false
        }

        return true
    }

    private fun isLocalAddressEmailValid(local: String): Boolean {
        if (local.length > 64)
            return false

        // Verifies if the (.) is at the start or end of localAddress
        if (local.contains('.')) {
            //verifies if it starts or ends with dot (.)
            if (local[0] == '.' || local[local.length - 1] == '.')
                return false

            // Tests if there are two or more points consecutively
            var count = 0;
            local.forEach { c: Char ->
                if (c == '.') {
                    count += 1
                    if (count > 1) {
                        return false
                    }
                } else {
                    count = 0;
                }
            }
        }

        return true
    }
}