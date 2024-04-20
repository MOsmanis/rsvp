package wedding;

import java.io.Serializable;

public record GuestDTO(String name, String familyName, String dietaryPreference, boolean isComing)
    implements Serializable {
}
