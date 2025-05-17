package services;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.*;

public class Validation {

    private static final Set<ErrorClass> errors = new HashSet<>();
    private static boolean result = true;

        /* AUTHOR */

    public static Set<ErrorClass> validateDataAuthor(int option, Author author) {
        errors.clear();
        result = true;

        validateName(author.getName());
        validateArtisticName(author.getArtisticName());
        validateBirthday(author.getBirthday());
        validateGender(author.getGender());
        validateBiography(author.getBiography());

        if (option == 1) {
            validatePicture(author.getPicture());
            validateEmail(author.getEmail());
        }

        return errors;
    }

    private static void validateName(String name) {
        name = Optional.ofNullable(name).orElse("");
        if (name.isEmpty()) {
            errors.add(new ErrorClass(1, "Name is empty."));
            result = false;
        } else if (name.length() < 3) {
            errors.add(new ErrorClass(1, "Invalid character quantity for Name. It must be at least 3 characters."));
            result = false;
        }
    }

    private static void validateArtisticName(String artisticName) {
        artisticName = Optional.ofNullable(artisticName).orElse("").trim();
        if (artisticName.isEmpty()) {
            errors.add(new ErrorClass(2, "Artistic Name is empty."));
            result = false;
        } else if (artisticName.length() < 3) {
            errors.add(new ErrorClass(2, "Invalid character quantity for Artistic Name. It must be at least 3 characters."));
            result = false;
        }
    }

    private static void validateEmail(String email) {
        email = Optional.ofNullable(email).orElse("").trim();
        if (email.isEmpty()) {
            errors.add(new ErrorClass(3, "E-mail is empty."));
            result = false;
        } else if (!matchEmail(email)) {
            errors.add(new ErrorClass(3, "Invalid E-mail."));
            result = false;
        } else if (getEmail(email)) {
            errors.add(new ErrorClass(3, "E-mail is already registered."));
            result = false;
        }
    }

    private static boolean matchEmail(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    private static boolean getEmail(String email) {
        return Author.find.where().eq("email", email).findUnique() != null;
    }

    private static void validateBirthday(String birthday) {

        if (!birthday.isEmpty()) {
            boolean result = true;
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            try {
                java.time.LocalDate parsedDate = java.time.LocalDate.parse(birthday, formatter);
                // Você pode adicionar mais validações, como se a data está no passado, se necessário.
            } catch (java.time.format.DateTimeParseException e) {
                errors.add(new ErrorClass(4, "Invalid birthday."));
                result = false;
            }
        } else {
            errors.add(new ErrorClass(4, "Birthday is empty."));
            result = false;
        }
    }

    private static void validateGender(String gender) {
        gender = Optional.ofNullable(gender).orElse("").trim();
        if (gender.isEmpty()) {
            errors.add(new ErrorClass(5, "Gender is empty."));
            result = false;
        } else if (gender.length() != 1) {
            errors.add(new ErrorClass(5, "Invalid character quantity for Gender. Must be 1 character."));
            result = false;
        }
    }

    private static void validateBiography(String biography) {
        biography = Optional.ofNullable(biography).orElse("").trim();
        if (biography.isEmpty()) {
            errors.add(new ErrorClass(6, "Biography is empty."));
            result = false;
        } else if (biography.length() < 10) {
            errors.add(new ErrorClass(6, "Invalid character quantity for Biography. It must be at least 10 characters."));
            result = false;
        }
    }

    public static boolean deleteAuthor(int id) {
        return Book.find.where().eq("author_id", id).findList().isEmpty();
    }

        /* BOOK */

    public static Set<ErrorClass> validateDataBook(Integer opc, Book book) {

        Validation.result = true;
        Validation.errors.clear();
        Validation.validateTitle(book.getTitle());
        Validation.validatePrice(book.getPrice());

        if (opc == 1) {
            Validation.validatePicture(book.getPicture());
            Validation.validateISBN(book.getIsbn());
        }

        return errors;
    }

    private static void validateTitle(String title) {

        if (title != "") {
            if (title.length() < 3) {
                Validation.errors.add(new ErrorClass(1, "Invalid character quantity for title. It must be above 3 characters."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add(new ErrorClass(1, "Title is empty."));
            Validation.result = false;
        }
    }

    public static void validateISBN(String isbn) {
        if (isbn != null && !isbn.trim().isEmpty()) {

            String cleanIsbn = isbn.replaceAll("[-\\s]", "");

            if (!validateChecksum(cleanIsbn)) {
                Validation.errors.add(new ErrorClass(2, "Invalid ISBN."));
                Validation.result = false;
            } else {
                if (getISBN(cleanIsbn)) {
                    Validation.errors.add(new ErrorClass(2, "ISBN is already registered."));
                    Validation.result = false;
                }
            }

        } else {
            Validation.errors.add(new ErrorClass(2, "ISBN is empty."));
            Validation.result = false;
        }
    }

    private static boolean validateChecksum(String isbn) {
        if (isbn.length() != 13 || !isbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int checksum = (10 - (sum % 10)) % 10;
        int lastDigit = Character.getNumericValue(isbn.charAt(12));

        return checksum == lastDigit;
    }

    private static boolean getISBN(String isbn) {
        Book book = Book.find.where().eq("isbn", isbn).findUnique();
        return book != null;
    }

    private static void validatePrice(Double price) {

        if (price.toString() != "") {
            if (price < 0) {
                Validation.errors.add(new ErrorClass(3, "Invalid Price."));
                Validation.result = false;
            }
        } else {
            Validation.errors.add(new ErrorClass(3, "Price is empty."));
            Validation.result = false;
        }
    }

    private static void validatePicture(byte[] picture) {

        if (picture.length == 0) {
            Validation.errors.add(new ErrorClass(7, "Picture is empty."));
            Validation.result = false;
        }
    }

    //Login's Validations

    public static Set<ErrorClass> validateDataLogin(Usersys usersys) {
        Validation.result = true;
        Validation.errors.clear();
        Validation.validateUsername(usersys.getUsername());
        Validation.validatePassword(usersys.getUserpass());
        return errors;
    }

    private static void validateUsername(String username) {
        if (username.equals("")) {
            Validation.errors.add(new ErrorClass(1, "Username is empty."));
            Validation.result = false;
        }
    }

    private static void validatePassword(String password) {
        if (password.equals("")) {
            Validation.errors.add(new ErrorClass(1, "Password is empty."));
            Validation.result = false;
        }
    }
}