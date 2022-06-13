package com.example.Spring_Security_5.Utility;

import com.example.Spring_Security_5.Security.Secret.Secret;
import com.example.Spring_Security_5.User.Gender;
import com.example.Spring_Security_5.User.Role.Role;
import com.example.Spring_Security_5.User.Role.UserRole;
import com.example.Spring_Security_5.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class CSVParser implements Serializable{
    private static final Logger log = LoggerFactory.getLogger(CSVParser.class);
    protected HierarchicalCheck hck = new HierarchicalCheck();
    private Map<Integer, Object[]> file_contents = new HashMap<>();
    /**
     * Negative lookahead provides the solution: q(?!...). The negative
     * lookahead construct is the pair of parentheses, with the opening
     * parenthesis followed by a question mark and an exclamation point.
     * Inside the lookahead, we have the trivial regex u.
     * Positive lookahead works just the same. q(?=u) matches a q that is
     * followed by a u, without making the u part of the match. The positive
     * lookahead construct is a pair of parentheses, with the opening parenthesis
     * followed by a question mark and an equals sign.
     */
    private final String COMMA_REGEX = String.format(",");
    private final String COMMA_QUOTE_POSITIVE_LOOKAHEAD_REGEX = String.format(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    private FileParser fp = null;

    Scanner csv_file ;
    CSVParser(){}
    public CSVParser(FileParser fp) throws FileNotFoundException {
        this.fp = fp;
        this.csv_file = new Scanner(this.fp.getNext());
    }
    CSVParser(File file) throws FileNotFoundException {
        this.fp = new FileParser(file);
        this.csv_file = new Scanner(this.fp.getNext());
    }

    public FileParser getFp() {
        return this.fp;
    }

    public void setFp(FileParser fp) {
        this.fp = fp;
    }

    public Object getRow(int row){
        return this.file_contents.get(row);
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CSVParser)) return false;
        final CSVParser other = (CSVParser) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$fp = this.getFp();
        final Object other$fp = other.getFp();
        if (!Objects.equals(this$fp, other$fp)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CSVParser;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $fp = this.getFp();
        result = result * PRIME + ($fp == null ? 43 : $fp.hashCode());
        return result;
    }

    /**
     * Initialize file contents
     * @param regexSeparator regulay expression csv separator
     */
    private void CSVInit(String regexSeparator){
        int cnt = 0;
        while (this.csv_file.hasNext()){

            this.file_contents.put(cnt, csv_file.nextLine().split(regexSeparator));
            cnt++;
        }
    }

    /**
     * Gets User/Student info from CSV file
     * @return {@literal List<User>}
     */
    public List<User> getUsersFromCsvFile(){
        int cnt = 0;
        List<User> users = new ArrayList<>();
        CSVInit(COMMA_REGEX);
        Role STUDENT = new Role(UserRole.STUDENT,"STUDENT");
//       System.out.println(csv.getRow(9).toArray()[0]);
        for(int i = 1; i < file_contents.size(); i++)
        {
            log.info("User: {}", Arrays.stream(file_contents.get(i)).collect(Collectors.toList()));
            List<Object> lUser = Arrays.stream(file_contents.get(i)).collect(Collectors.toList());
            if (String.valueOf(lUser.get(6)).equalsIgnoreCase( "STUDENT" )){
                log.info(String.valueOf(lUser.get(6)));
                log.info("Size: {}",String.valueOf(lUser.get(6)));
                cnt++;
                User user = new User(
                        lUser.get(0).toString().trim(),
                        lUser.get(1).toString().trim(),
                        new Secret(lUser.get(2).toString().trim()),
                        (lUser.get(3).toString().trim().equalsIgnoreCase("FEMALE") ? Gender.FEMALE : Gender.MALE),
                        LocalDate.of(Integer.parseInt(lUser.get(4).toString().trim().split("-")[0]),
                                Month.of(Integer.parseInt(lUser.get(4).toString().trim().split("-")[1])),
                                Integer.parseInt(lUser.get(4).toString().trim().split("-")[2])),
                        lUser.get(5).toString().trim(),
                        Set.of(STUDENT)
                );
                users.add(user);
                log.info(user.toString());
            }
            // limit the amount of dummy Users added to DB
            if(i == 1000){break;}
        }
        log.info("Total Users: {}",cnt);
        return users;
    }


///////////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) throws IOException {
        CSVParser csv = new CSVParser(new FileParser(new File("src/main/resources/users.csv")));
        log.info("ABS Path: " + csv.getFp().getNext().getAbsolutePath());
        log.info("Is File: " + csv.getFp().getNext().isFile());
        log.info(csv.getFp().getNext().toString());

    }

}