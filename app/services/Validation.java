package services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.*;

public class Validation {

    private static boolean result = true;

    private static Set<ErrorClass> errors = null;

    static {
        errors = new HashSet<> ();
    }

    //Completed
    public static Set<ErrorClass> ValidateDataAuthor (String name, String artisticName, String email, Date birthday, String gender, String biography, byte[] picture) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateName (name);
        Validation.ValidateArtisticName (artisticName);
        Validation.ValidateEmail (email);
        //Validation.ValidateBirthday (birthday);
        Validation.ValidateGender (gender);
        Validation.ValidateBiography (biography);
        Validation.ValidatePicture (picture);
        return Validation.errors;
    }

    //Edition Validation
    //Without Picture's Validation
    public static Set<ErrorClass> ValidateDataAuthor (String name, String artisticName, String email, Date birthday, String gender, String biography) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateName (name);
        Validation.ValidateArtisticName (artisticName);
        Validation.ValidateEmail (email);
        //Validation.ValidateBirthday (birthday);
        Validation.ValidateGender (gender);
        return Validation.errors;
    }

    //Without Email and Picture's Validation
    public static Set<ErrorClass> ValidateDataAuthor (String name, String artisticName, Date birthday, String gender, String biography) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateName (name);
        Validation.ValidateArtisticName (artisticName);
        //Validation.ValidateBirthday (birthday);
        Validation.ValidateGender (gender);
        return Validation.errors;
    }

    private static void ValidateName (String name) {

        if (name != "") {
            if (name.length () < 3) {
                Validation.errors.add (new ErrorClass (1, "Invalid character quantity for Name. It must be above 3 characters."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (1, "Name is empty."));
            Validation.result = false;
        }
    }

    private static void ValidateArtisticName (String artisticName) {

        if (artisticName != "") {
            if (artisticName.length () < 3) {
                Validation.errors.add (new ErrorClass (2, "Invalid character quantity for Artistic Name. It must be above 3 characters."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (2, "Artistic Name is empty."));
            Validation.result = false;
        }
    }


    private static void ValidateEmail (String email) {

        if (email != "") {
            if (Validation.MatchEmail (email)) {
                if (Validation.GetEmail (email)) {
                    Validation.errors.add (new ErrorClass (3, "E-mail is already registered."));
                    Validation.result = false;
                }
            } else {
                Validation.errors.add (new ErrorClass (3, "Invalid E-mail."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (3, "E-mail is empty."));
            Validation.result = false;
        }
    }

    private static boolean MatchEmail (String email) {
        String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (email);
        return matcher.matches ();
    }

    private static boolean GetEmail (String email) {

        Author author = Author.find.where().eq("email", email).findUnique();
        if(author == null){
            return false;
        }
        return true;
    }

    private static void ValidateBirthday (String birthday) {

        //
    }

    private static void ValidateGender (String gender) {

        if (gender != "") {
            if (!(gender.length () == 1)) {
                Validation.errors.add (new ErrorClass (5, "Invalid character quantity for Gender. Must be 1 character."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (5, "Gender is empty."));
            Validation.result = false;
        }
    }

    private static void ValidateBiography (String biography) {

        if (biography != "") {
            if (biography.length () < 10) {
                Validation.errors.add (new ErrorClass (6, "Invalid character quantity for Biography. It must be above 10 characters."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (6, "Biography is empty."));
            Validation.result = false;
        }
    }

    public static boolean DeleteAuthor (int id) {

        List<Book> book = Book.find.where().eq("author_id",id).findList();
        if(book.size() == 0)
        {
            return true;
        }
        return false;
    }

    /* Book's Validations */

    public static Set<ErrorClass> ValidateDataBook (String title, String isbn, Float price, byte[] picture) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateTitle (title);
        Validation.ValidateISBN (isbn);
        Validation.ValidatePrice (price);
        Validation.ValidatePicture (picture);
        return errors;
    }

    public static Set<ErrorClass> ValidateDataBook (String title, Float price, byte[] picture) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateTitle (title);
        Validation.ValidatePrice (price);
        Validation.ValidatePicture (picture);
        return errors;
    }

    public static Set<ErrorClass> ValidateDataBook (String title, Float price) {

        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateTitle (title);
        Validation.ValidatePrice (price);
        return errors;
    }

    private static void ValidateTitle (String title) {

        if (title != "") {
            if (title.length () < 3) {
                Validation.errors.add (new ErrorClass (1, "Invalid character quantity for title. It must be above 3 characters."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (1, "Title is empty."));
            Validation.result = false;
        }
    }

    private static void ValidateISBN (String isbn) {

        if (isbn != "") {
            if (isbn.length () == 13) {
                if (Validation.GetISBN (isbn)) {
                    Validation.errors.add (new ErrorClass (2, "ISBN is already registered."));
                    Validation.result = false;
                }
            } else {
                Validation.errors.add (new ErrorClass (2, "Invalid character quantity for ISBN. Must be 13 character."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (2, "ISBN is empty."));
            Validation.result = false;
        }
    }

    private static boolean MatchISBN (String isbn) {
        String regex = "^\\d{13}$";
        Pattern pattern = Pattern.compile (regex);
        Matcher matcher = pattern.matcher (isbn);
        return matcher.matches ();
    }

    private static boolean GetISBN (String isbn) {
        Book book = Book.find.where().eq("isbn", isbn).findUnique();
        if (book != null) {
            return true;
        }
        return false;
    }

    private static void ValidatePrice (Float price) {

        if (price.toString () != "") {
            if (price < 0) {
                Validation.errors.add (new ErrorClass (3, "Invalid Price."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add (new ErrorClass (3, "Price is empty."));
            Validation.result = false;
        }
    }

    private static void ValidatePicture (byte[] picture) {

        if (picture.length == 0) {
            Validation.errors.add (new ErrorClass (7, "Picture is empty."));
            Validation.result = false;
        }
    }

    //Login's Validations

    public static Set<ErrorClass> ValidateDataLogin (String username, String password) {
        Validation.result = true;
        Validation.errors.clear ();
        Validation.ValidateUsername (username);
        Validation.ValidatePassword (password);
        return errors;
    }

    private static void ValidateUsername(String username) {
        if (username.equals("")) {
            Validation.errors.add (new ErrorClass (1, "Username is empty."));
            Validation.result = false;
        }
    }

    private static void ValidatePassword (String password) {
        if (password.equals("")) {
            Validation.errors.add (new ErrorClass (1, "Password is empty."));
            Validation.result = false;
        }
    }


}