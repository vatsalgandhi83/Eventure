package com.eventure.events.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private double latitude;
    private double longitude;
    private String gmapUrl;

    public String getGmapUrl() { return gmapUrl; }
    public void setGmapUrl(String gmapUrl) { this.gmapUrl = gmapUrl; }
}

