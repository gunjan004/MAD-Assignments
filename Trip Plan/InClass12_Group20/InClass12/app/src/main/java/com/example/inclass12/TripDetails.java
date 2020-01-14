package com.example.inclass12;

import java.io.Serializable;
import java.util.List;

public class TripDetails implements  Serializable {



    public List<PlacesData> userRestaurents ;

    public String tripId;
    public String userId;
    public String userName;
    public String TripName;
    public String TripDate;
    public String destinationCity;



    public TripDetails()
    {

    }

}
