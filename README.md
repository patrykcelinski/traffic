# Traffic Data Exercise

This is a programming exercise that involves reading a data file that contains information about the traffic flow on some fictitious city streets. To keep this simple, the streets are all one-way. The program is to be written in Scala using a functional style of programming.

The streets are mostly in a grid pattern. Streets identified by numbers (1, 2, 3, ...) run from east to west or from west to east with Street 1 being the northernmost street. Avenues identified by letters (A, B, C, ...) run from north to south or from south to north, with avenue A being the westernmost avenue. There is also an elevated expressway that connects more than two intersections and does not follow the grid pattern.

The program will need to read the data and then find good routes between different pairs of intersections. Intersections are identified by the names of the avenue and street that intersect at the intersection. For example, an intersection may be identified by the combination of Avenue F and street 24.

The input to the program will consist of the data file and two intersections. It must be possible to specify the location of the data file and the names of the two intersections on the command line. The program output will describe a good route to travel from the first intersection to the second.

### Detailed requirements

The program will begin by reading a data file. You can copy the data file from [this link](https://drive.google.com/file/d/1Y73I-jLXT8XmlwpkdSlKKujVyKsExvLc/view) and have the program read the local copy or you can have the program read the file from the URL. Either is acceptable.

The data file contains data that describes the flow of traffic through the streets measured at different points in time. The format of the data is JSON. It looks like this:

```
{
    "trafficMeasurements": [
        {
            "measurementTime": 83452,
            "measurements": [
                {
                    "startAvenue": "A",
                    "startStreet": "1",
                    "transitTime": 59.57363899660943,
                    "endAvenue": "A",
                    "endStreet": "2"
                },
                {
                    "startAvenue": "A",
                    "startStreet": "2",
                    "transitTime": 40.753916740023314,
                    "endAvenue": "A",
                    "endStreet": "3"
                },
                ⋮
            ]
        },
        {
            "measurementTime": 83556,
            "measurements": [
                ⋮
            ]
        ]
}
```

The file contains a single JSON object. The JSON object has a field named `trafficMeasurements`. The field's value is an array of JSON objects. Each JSON object contains a set of traffic flow measurements made at a different point in time. The first element of the array is the earliest measurement set. The last element of the array is the most recent measurement set.

These measurement set objects contain a `measurementTime` field that indicates when the measurements were made. The measurement set objects also contain a `measurements` field that is an array of objects that correspond to the individual measurements.

These measurement objects each correspond to a road segment that goes from one intersection to another. The `startAvenue` and `startStreet` fields identify the intersection that the road segment goes from. The `endAvenue` and `endStreet` fields identify the intersection that the road segment goes to. The value of the `transitTime` field is the amount of time it is taking to traverse the road segment.

When the program is selecting a good route from one intersection to the other, it should select the route with the smallest total transit time. The transit times for each road segment vary from one measurement to the next. You should base the transit time on some sort of average for each road segment.

If you feel that it will take too long to write code to find the best route, write the program to find any route. If you choose this option, expect that during our technical interview we will ask you how you would change it to find the fastest route.

The output of the program should be JSON. It should include:

- the starting intersection
- the ending intersection
- an array that contains the sequence of road segments to be traversed to get from the starting intersection to the ending intersection.
- the total transit time based on the road segments in the route
