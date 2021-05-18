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

## Generate edges.txt
Solr-8.8.2 has Lucene as the default ranking algorithm. As such, does not provide any direct way to utilize PageRank. However, we can use an external file with calculated PageRank as an event listener to get the results we get using PageRank.

For this, we need to calculate the Links (outgoing and incoming) for each individual html document and prepare an edge list which can be used to calculate PageRank. To generate said file, follow the steps below.

1.	Download JSoup.jar to external library of ExtractingLinks.java
2.	Build a map with keys as the URL and values as the id from the file “URLtoHTML_latimes_news.csv”
3.	Extract all the outgoing links by JSoup’s function.
4.	Add the outgoing links and their ids to the edge list if map contains the outgoing link.
5.	Save all the extracted links in “edges.txt”

## Generate external_pageRankFile.txt
We use a NetworkX library to compute PageRank using the edges.txt as input. This PageRank function takes a NetworkX graph (which can be constructed from edges.txt) as input and returns a dictionary of graph nodes with corresponding PageRank scores.

1.	Compute PageRank using the following parameters: alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight', dangling=None
2.	Run pageRank.py to generate external_pageRankFile.txt. 
3.	Place the external_pageRankFile.txt in the data folder of the example1 core in Solr-8.8.2 folder.

## Adding PageRank to Solr
In order to add the PageRank to Solr, we need to define the external_pageRankFile.txt as an external field in Solr managed-schema and add event listeners in solrconfig.xml.

1.	Add the following fields to managed-schema: 
 <fieldType name=”external” keyField=”id” defVal=”0” class=”solr.ExternalFileField”/>
<field name=”pageRankFile” type=”external” stored=”false” indexed=”false”/>
![image](https://user-images.githubusercontent.com/55113221/118718968-bc040400-b7dc-11eb-9dbe-a1a39beb3c1e.png)
2.	Define these listeners within the <query> element in the solrconfig.xml file:
 <listener event=”newSearcher” class=”org.apache.solr.schema.ExternalFileFieldReloader”/> 
<listener event=”firstSearcher” class=”org.apache.solr.schema.ExternalFileFieldReloader”/>
![image](https://user-images.githubusercontent.com/55113221/118719000-c6be9900-b7dc-11eb-94b6-0ba4ee7ddf38.png)
3.	Reload the index, by going to the Solr Dashboard UI -> Core Admin and clicking on the “Reload” button.
  
## Setting up the Webpage
Now that the PageRank is added to Solr, we need an interface to interact and collect data, to compare the two ranking algorithms. For that, we are going to write a PHP webpage which sends a request to Solr and utilizes and displays the search results.

1.	Set the parameters of Lucene and PageRank algorithm:
lucene: fl: title,og_url,og_description,id
pageRank: fl: title,og_url,og_description,id; sort: pageRankFile desc
2.	Create a new solr service instance, we send request to https://localhost, port: 8983 and path: /solr/example1.
3.	Get the response and show 4 fields in html: title(or NA), URL, description(or NA), id.

## Analyzing and Comparing the Ranking Algorithms
Once the setup is completed, we can use the webpage we wrote to find out the search results for different queries. The following pictures are screenshots of the search results for term “Cannes” in the search engine. We can also see that all the links shown are redirected to their host site.

## Overlap
We can see much difference between the two ranking algorithms as there is no significant overlap between the two. We can only observe a single overlap on a single query. The overlap graph looks as follows.
<img width="526" alt="Overlap graph" src="https://user-images.githubusercontent.com/55113221/118719102-e48bfe00-b7dc-11eb-949b-a5f1ef066a14.png">
