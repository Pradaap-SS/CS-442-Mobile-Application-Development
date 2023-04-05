package com.example.civiladvocacyapp;

import java.io.Serializable;

public class Officials implements Serializable {
    private String name, title, party,
            photoURL = null, fbAccountLink = null, twitterAccountLink = null,
            youtubeAccountLink = null, address = null, city = null, state = null,
            zip = null, phone = null, email = null, website = null;

    public Officials(String name,
                     String title,
                     String party,
                     String photoURL,
                     String fbAccountLink,
                     String twitterAccountLink,
                     String youtubeAccountLink,
                     String address,
                     String city,
                     String state,
                     String zip,
                     String phone,
                     String email,
                     String website) {
        this.name = name;
        this.title = title;
        this.party = party;
        this.photoURL = photoURL;
        this.fbAccountLink = fbAccountLink;
        this.twitterAccountLink = twitterAccountLink;
        this.youtubeAccountLink = youtubeAccountLink;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getParty() {
        return party;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getFbAccountLink() {
        return fbAccountLink;
    }

    public String getTwitterAccountLink() {
        return twitterAccountLink;
    }

    public String getYoutubeAccountLink() {
        return youtubeAccountLink;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public Officials() {
        this.name = "pradaap";
        this.title = "captain";
        this.party = "party";
        this.address = "prairie shores";
        this.phone = "312-111-1100";
        this.email = "pradaap@gmail.com";
        this.website = "abcdef.gov";
        this.fbAccountLink = "myFb.com";
        this.twitterAccountLink = "myTwitter.com";
        this.youtubeAccountLink = "myYouTube.com";
    }
    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    @Override
    public String toString() {
        return "Official{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", party='" + party + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", fbAccountLink='" + fbAccountLink + '\'' +
                ", twitterAccountLink='" + twitterAccountLink + '\'' +
                ", youtubeAccountLink='" + youtubeAccountLink + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
