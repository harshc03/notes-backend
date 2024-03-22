package rs.lab.notes.util;

import org.springframework.data.domain.Pageable;

public class StringHelper {

    private StringHelper() {
    }

    public static String camelToSnake(String str)
    {
 
        // Empty String
        String result = "";
 
        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);
 
        // Traverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {
 
            char ch = str.charAt(i);
 
            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                    = result
                      + Character.toLowerCase(ch);
            }
 
            // If the character is lower case then
            // add such character into result string
            else {
                result = result + ch;
            }
        }
 
        // return the result
        return result;
    }
    
    public static void appendOrderBy(StringBuilder sb, Pageable pageable) {
        
        if (pageable.getSort() != null && !pageable.getSort().isEmpty()) {
            sb.append(" ORDER BY ");
            for (var order : pageable.getSort()) {
                sb.append(StringHelper.camelToSnake(order.getProperty())).append(' ').append(order.getDirection());
            }
        }
        
    }
    
    public static void appendOffsetLimit(StringBuilder sb, Pageable pageable) {
        sb.append(String.format(" OFFSET %d ROWS FETCH NEXT %d ROWS ONLY", pageable.getOffset(), pageable.getPageSize()));
    }
}
