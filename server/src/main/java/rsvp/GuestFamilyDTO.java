package wedding;

import java.io.Serializable;
import java.util.List;

public record GuestFamilyDTO(String id, String welcomeMsg, List<GuestDTO> members, boolean responded,
                             String lastResponseDate)
    implements Serializable {
}
