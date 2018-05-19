package com.udacity.sandwichclub.utils;

import android.util.JsonReader;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class JsonUtils {

    private static final String LOG_LABEL = "JsonUtils";

    /**
     * Parse a JSON string into a {@link Sandwich} object, without the use of any 3rd party
     * libraries.
     *
     * The field names match exactly, but the structure does not, in that the field `name`
     * is in the JSON an object of its own, containing the fields `mainName` and `alsoKnownAs`.
     *
     * @param json JSON string to parse
     * @return Sandwich object, or null if parsing failed
     */
    public static Sandwich parseSandwichJson(String json) {
        Log.d(LOG_LABEL, "JSON string to parse: ");
        Log.d(LOG_LABEL, json);

        JsonReader reader = new JsonReader(new StringReader(json));

        try {
            // initialize to empty strings and empty arrays (no fields are required)
            String mainName = "";
            String placeOfOrigin = "";
            String description = "";
            String image = "";
            ArrayList<String> alsoKnownAs = new ArrayList<>();
            ArrayList<String> ingredients = new ArrayList<>();

            // read JSON
            reader.beginObject();
            while (reader.hasNext()) {
                // get the field name
                String name = reader.nextName();

                switch (name) {
                    case ("name"):
                        // parse nested object
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String nestedName = reader.nextName();
                            if (nestedName.equals("mainName")) {
                                mainName = reader.nextString();
                            } else if (nestedName.equals("alsoKnownAs")) {
                                // an array of strings
                                reader.beginArray();
                                while (reader.hasNext()) {
                                    alsoKnownAs.add(reader.nextString());
                                }
                                reader.endArray();
                            } else {
                                Log.w(LOG_LABEL, "Unrecognized field on 'name' JSON object: " + nestedName);
                            }
                        }
                        // close name object
                        reader.endObject();
                        break;
                    case ("placeOfOrigin"):
                        placeOfOrigin = reader.nextString();
                        break;
                    case("description"):
                        description = reader.nextString();
                        break;
                    case("image"):
                        image = reader.nextString();
                        break;
                    case("ingredients"):
                        // an array of strings
                        reader.beginArray();
                        while (reader.hasNext()) {
                            ingredients.add(reader.nextString());
                        }
                        reader.endArray();
                        break;
                    default:
                        Log.w(LOG_LABEL, "Unrecognized JSON field named " + name);
                }

            }

            // close top-level object
            reader.endObject();

            return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description,
                    image, ingredients);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
