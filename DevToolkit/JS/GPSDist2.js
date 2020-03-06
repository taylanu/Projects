// https://stackoverflow.com/questions/365826/calculate-distance-between-2-gps-coordinates
// https://stackoverflow.com/questions/6366408/calculating-distance-between-two-latitude-and-longitude-geocoordinates
function distanceCalc(lat1, lon1, lat2, lon2, unit) {
      var rlat1 = Math.PI * lat1/180
      var rlat2 = Math.PI * lat2/180
      var rlon1 = Math.PI * lon1/180
      var rlon2 = Math.PI * lon2/180
      var theta = lon1-lon2
      var rtheta = Math.PI * theta/180
      var dist = Math.sin(rlat1) * Math.sin(rlat2) + Math.cos(rlat1) * Math.cos(rlat2) * Math.cos(rtheta);
      
      dist = Math.acos(dist)
      dist = dist * 180/Math.PI
      dist = dist * 60 * 1.1515
      if (unit=="K") { dist = dist * 1.609344 }
      if (unit=="N") { dist = dist * 0.8684 }
      return dist
}