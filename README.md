# Lucene Vs PageRank

## Overview
The document looks at a difference between two ranking algorithms for webpages. The algorithms were tested on same data, a compilation of webpages from “Los Angeles Times”. A total of 19193 webpages were scraped and used to compare using Lucene and PageRank. The webpages were indexed in Solr. 

## Installation and Setup
Indexing HTML files in Solr
1.	Download Solr-8.8.2
2.	Start Solr server, cd into Solr-8.8.2 and enter the following command in the terminal:
bin/solr start

3.	Create a new core named example1 using command: 
bin/solr create -c example1

4.	Index the HTML pages by entering the following command: 
bin/post -c example1 -filetypes html /Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/latimes

5.	Once the indexing is done, you can verify it on Solr UI. Go to the link http://localhost:8983/solr/. Select the core example1 in the dropdown menu.
![image](https://user-images.githubusercontent.com/55113221/118717981-97f3f300-b7db-11eb-8c83-c6ad289bf336.png)

## Generate edges.txt
Solr-8.8.2 has Lucene as the default ranking algorithm. As such, does not provide any direct way to utilize PageRank. However, we can use an external file with calculated PageRank as an event listener to get the results we get using PageRank.

For this, we need to calculate the Links (outgoing and incoming) for each individual html document and prepare an edge list which can be used to calculate PageRank. To generate said file, follow the steps below.

1.	Download JSoup.jar to external library of ExtractingLinks.java
2.	Build a map with keys as the URL and values as the id from the file “URLtoHTML_latimes_news.csv”
3.	Extract all the outgoing links by JSoup’s function.
4.	Add the outgoing links and their ids to the edge list if map contains the outgoing link.
5.	Save all the extracted links in “edges.txt”
![image](https://user-images.githubusercontent.com/55113221/118718084-b3f79480-b7db-11eb-8773-e42eae847298.png)
