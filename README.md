# CodeFoo7
CodeFoo2017

<p>
<b>1) My CodeFoo Video Link:</b>
<br/>
<a href="https://www.youtube.com/watch?v=dZcPul43QX8">https://www.youtube.com/watch?v=dZcPul43QX8</a>
</p>
<hr/>

<b> 2) King Kong Questions:</b>

<p> 
According to the <a href="http://www.esbnyc.com/sites/default/files/esb_fact_sheet_4_9_14_4.pdf">Empire State Realty Trust</a>, the Empire State building stands at 1,250 ft. tall with an antenna that reaches an additional 204 ft. The building is about 187 ft. wide and 424 ft. long with an area of about 79,288 square ft. and volume of 37 million cubic feet. So to create a rough estimate I will attempt to find how many logs are in single cubic foot block then multiple by the total cubic feet of the building. If we assume we are using standard Lincoln logs of about 10 ½ inches in length and ¾ inches in height, then we can construct a hollow cube with 4 sides of 16 logs that reaches 1 ft. in height. However, this cube has only a width and length of only 10½ inches. Therefore, 1 cubic foot of logs is roughly equal to (4 sides * 16 logs) 64 logs plus an additional:
<br/><br/>
(10½ / 12 = .875) // length of logs divided by total length of a foot in inches
<br/>
(1 - .875 = .125)  // 1 minus the percentage of 12 inches that have already been covered
<br/>
(64 * .125 = 8) 	 // 8 equals number of logs missing from the block of 64 logs to cover an entire 1 cubic foot
<br/>
<br/>
8 logs, which is a total of 72 logs per cubic foot. Now that I have estimated the number of logs in a single cubic block I must multiply the number of logs by the total cubic ft. of the building. Taking this into account, 37 million multiplied by 72 is roughly equal to <b>2.664 billion logs</b>.

</p> 
<hr/>

<b>3) Grid Solution (Ruby):</b>
<br/>
<br/>
<p/>
This algorithm builds a grid of random single digit numbers and finds all possible combinations that add up to the area of the grid. The program builds the grid then computes the chains starting at every x,y index inside the grid. First, we test that the index exists. The function then recursively calls itself to check the value of every index that surrounds x,y. For each index we check, we add all the indexes that have proven to exist. If the sum of the index values is less than the area of the grid we again recursively call the function to search further chains. When the sum equals the area of the grid we add the chain to a data structure of valid answers. We check that each new chain added to our list of answers is unique by sorting the answers then comparing their contents. The answer must also have a total number of values that is at least the width of the grid minus one. The final list of answers is printed to the screen:
</p>
<br/>
<br/>
<img src="img/grid2.JPG" width="340"/>
<br/>
<br/>
<p>
I've also implemented this ruby script to take two parameters, width and height. The results take much longer to process as more combinations and answers become possible:
</p>
<br/>
<br/>
<img src="img/grid1.JPG" width="340"/>
<hr/>

<b>4) Android App (Java):</b>
<br/><br/>
<p>
This Android (4.4 KitKat minimum) application gathers and displays data from IGN's webapi. I build a List View which contains list objects that include articles, videos, and video lists. The app dynamically loads data as the user scrolls. The user can navigate to the content of the article in a web view. For additional features I want to make use of the tags that were provided in the api. I first considered a search bar but was reminded of how IGN's page is organized. The tabs that let you navigate between articles relating to PC, PS4, XBOX etc. are extremely convenient and were a feature I wanted to implement. I designed my Android application with a horizontally scrollable list of tabs to navigate between articles related to the users taste or interest.
</p>
<br/>
<img src="img/android1.JPG" width="160"/>
<img src="img/android2.JPG" width="160"/>
<hr/>

<b>5) Front End (JavaScript):</b>
<br/><br/>
<p>
This web application was built to match IGN's design and be assecable for all devices. The application will also dynamically load data and allows to be sorted by catagory similar to the Android applications added functionality. 
</p>
<br/>
<img src="img/webapp1.JPG" width="300"/>
<hr/>

<b>Bonus (HTML5):</b>
<p>
Qwirkle! The most challenging assignment of them all! My high score is 131. (instructions included)
</p>
<br/>
<img src="img/qwirkle1.JPG" width="340"/>
<hr/>
