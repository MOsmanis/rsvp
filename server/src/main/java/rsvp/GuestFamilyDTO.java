package rsvp;

import java.io.Serializable;
import java.util.List;

public record GuestFamilyDTO(String id, String welcomeMsg, List<GuestDTO> members, String lastResponseDate)
    implements Serializable {
}
