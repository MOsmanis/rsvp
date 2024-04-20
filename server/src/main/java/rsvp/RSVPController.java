package rsvp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class RSVPController {
    private final RSVPFileDAO rsvpFileDAO;

    @Value("${rsvp.timezone}")
    private String timezone;

    @Autowired
    public RSVPController(RSVPFileDAO rsvpFileDAO)
    {
        this.rsvpFileDAO = rsvpFileDAO;
    }

    @GetMapping("/guests")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> getAllGuests() throws IOException, ClassNotFoundException
    {
        return rsvpFileDAO.getAllFamilies().stream().flatMap(f -> f.members().stream()).collect(Collectors.toList());
    }

    @GetMapping("/family/{familyId}")
    @ResponseStatus(HttpStatus.OK)
    public GuestFamilyDTO getFamily(@PathVariable("familyId") String familyId) throws IOException,
        ClassNotFoundException
    {
        return rsvpFileDAO.getAllFamilies().stream().filter(f -> familyId.equals(f.id())).findFirst().get();
    }

    @RequestMapping(value = "/submit", method = POST, consumes = APPLICATION_JSON_VALUE, produces =
        APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void updateFamily(@RequestBody GuestFamilyDTO request) throws ClassNotFoundException, IOException
    {
        String sentAt = ZonedDateTime.now(ZoneId.of(this.timezone))
            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        GuestFamilyDTO updatedFamily = new GuestFamilyDTO(request.id(), request.welcomeMsg(), request.members(), sentAt);
        rsvpFileDAO.save(updatedFamily);
        //TODO call email service
    }

    @ExceptionHandler({IOException.class, ClassNotFoundException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleFileExceptions() {
        return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
    }
}
