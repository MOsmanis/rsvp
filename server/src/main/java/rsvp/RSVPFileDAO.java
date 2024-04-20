package rsvp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RSVPFileDAO {

    private static final String BACKUP_PATH = "/opt/backups/";
    private final String messagesFilePath;

    public RSVPFileDAO(@Value("${dao.file}") String filePath) throws IOException
    {
        Assert.notNull(filePath,"Property 'dao.file' is not set");
        this.messagesFilePath = filePath;
        File messsagesFile = new File(filePath);
        boolean newCreated = messsagesFile.createNewFile();
        if (newCreated) {
            this.initGuests(messsagesFile);
        }
    }

    private synchronized void initGuests(File messsagesFile) throws IOException
    {
        ObjectOutputStream oos = null;
        try {
            FileOutputStream fos = new FileOutputStream(messsagesFile);
            oos = new ObjectOutputStream(fos);
            List<GuestFamilyDTO> families = new ArrayList<>(Arrays.asList(
                new GuestFamilyDTO("123","Guests", List.of(
                    new GuestDTO("name1", "surname1", "",false, ""),
                    new GuestDTO("name2", "surname1","",false, "")),
                    ""),
                new GuestFamilyDTO("356","Guests 2", List.of(
                    new GuestDTO("name2.1", "surname2", "",false, ""),
                    new GuestDTO("name2.2", "surname2", "",false, ""),
                    new GuestDTO("name2.3", "surname2", "",false, "")),
                    "")
            ));
            oos.writeObject(families);
            oos.close();
        } finally {
            if (oos!=null) {
                oos.close();
            }
        }
    }

    public synchronized ArrayList<GuestFamilyDTO> getAllFamilies() throws IOException, ClassNotFoundException
    {
        ObjectInputStream oos = null;
        try {
            FileInputStream fos = new FileInputStream(messagesFilePath);
            oos = new ObjectInputStream(fos);
            return (ArrayList<GuestFamilyDTO>)oos.readObject();
        } finally {
            if (oos!=null) {
                oos.close();
            }
        }
    }

    public synchronized void save(GuestFamilyDTO guestFamilyDTO) throws IOException, ClassNotFoundException
    {
        ArrayList<GuestFamilyDTO> families = this.getAllFamilies();
        try {
            createBackup(families);
        } finally {
            families =
                (ArrayList<GuestFamilyDTO>) families.stream().filter(f -> !guestFamilyDTO.id().equals(f.id())).collect(Collectors.toList());
            families.add(guestFamilyDTO);
            FileOutputStream fos = new FileOutputStream(messagesFilePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(families);
            oos.close();
        }
    }

    private void createBackup(ArrayList<GuestFamilyDTO> families) throws IOException, ClassNotFoundException
    {
        ObjectOutputStream oos = null;
        String backupPath = BACKUP_PATH + ZonedDateTime.now();
        File backupFile = new File(backupPath);
        File backupDir = new File(backupFile.getParentFile().getAbsolutePath());
        if(!backupDir.exists()) {
            backupDir.mkdirs();
        }

        try {
            FileOutputStream fos = new FileOutputStream(backupFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(families);
            oos.close();
        } finally {
            if (oos!=null) {
                oos.close();
            }
        }
    }
}
