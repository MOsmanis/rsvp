package rsvp;

import java.io.Serializable;

public record GuestDTO(String name, String familyName, String dietaryPreference, boolean isComing,
                       String latestUpdate)
    implements Serializable {
}
