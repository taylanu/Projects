#https://stackoverflow.com/questions/4913349/haversine-formula-in-python-bearing-and-distance-between-two-gps-points#4913653
#@author Michael Dunn
#@modified taylanu
from math import radians, cos, sin, asin, sqrt

# function can be implemented into another codebase, and values can be inserted in float form

def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points 
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians 
    #lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    lon1 = math.radians(lon1)
    lat1 = math.radians(lat1)
    lon2 = math.radians(lon2)
    lat2 = math.radians(lat2)

    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    dist_response = input("Do you use miles(M) or kilometers(K)")
    if(dist_response == 'M'):
        r = 3956 # Earth radius in miles.
    else if (dist_response == 'K':
        r = 6371 # Earth radius in kilometers.
    else:
        print("Please enter a preference.")
         
    return c * r