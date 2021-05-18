

Lucene Vs PageRank

HOMEWORK – 4

Overview

The document looks at a difference between two ranking algorithms for webpages. The algorithms were tested on same data,

a compilation of webpages from “*Los Angeles Times*”. A total of 19193 webpages were scraped and used to compare using

**Lucene** and **PageRank**. The webpages were indexed in *Solr***.**

Installation and Setup

Indexing HTML files in Solr

\1. Download Solr-8.8.2

\2. Start Solr server, cd into Solr-8.8.2 and enter the following command in the terminal:

**bin/solr start**

\3. Create a new core named example1 using command:

**bin/solr create -c example1**

\4. Index the HTML pages by entering the following command:

**bin/post -c example1 -filetypes html /Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/latimes**

\5. Once the indexing is done, you can verify it on Solr UI. Go to the link [http://localhost:8983/solr/.](http://localhost:8983/solr/)[ ](http://localhost:8983/solr/)Select the core

**example1** in the dropdown menu.

**Fig. 1. Solr UI**

Generate edges.txt

Solr-8.8.2 has Lucene as the default ranking algorithm. As such, does not provide any direct way to utilize PageRank. However,

we can use an external file with calculated PageRank as an event listener to get the results we get using PageRank.

For this, we need to calculate the Links (outgoing and incoming) for each individual html document and prepare an edge list

which can be used to calculate PageRank. To generate said file, follow the steps below.

\1. Download JSoup.jar to external library of ExtractingLinks.java

\2. Build a map with keys as the URL and values as the id from the file “**URLtoHTML\_latimes\_news.csv**”

\3. Extract all the outgoing links by JSoup’s function.





\4. Add the outgoing links and their ids to the edge list if map contains the outgoing link.

\5. Save all the extracted links in “**edges.txt**”

Generate external\_pageRankFile.txt

We use a **NetworkX** library to compute PageRank using the edges.txt as input. This PageRank function takes a NetworkX graph

(which can be constructed from edges.txt) as input and returns a dictionary of graph nodes with corresponding PageRank

scores.

\1. Compute PageRank using the following parameters: **alpha=0.85, personalization=None, max\_iter=30, tol=1e-06,**

**nstart=None, weight='weight', dangling=None**

\2. Run pageRank.py to generate **external\_pageRankFile.txt.**

\3. Place the **external\_pageRankFile.txt** in the data folder of the **example1** core in Solr-8.8.2 folder.

Adding PageRank to Solr

In order to add the PageRank to Solr, we need to define the **external\_pageRankFile.txt** as an external field in Solr **managed-**

**schema** and add event listeners in **solrconfig.xml.**

\1. Add the following fields to **managed-schema**:

**<fieldType name=”external” keyField=”id” defVal=”0” class=”solr.ExternalFileField”/>**

**<field name=”pageRankFile” type=”external” stored=”false” indexed=”false”/>**

Fig. 2. Adding external field to managed-schema

\2. Define these listeners within the **<query>** element in the **solrconfig.xml** file:

**<listener event=”newSearcher” class=”org.apache.solr.schema.ExternalFileFieldReloader”/>**

**<listener event=”firstSearcher” class=”org.apache.solr.schema.ExternalFileFieldReloader”/>**

Fig. 3. Adding listeners to solrconfig.xml

\3. Reload the index, by going to the Solr Dashboard UI -> Core Admin and clicking on the “Reload” button.

Setting up the Webpage

Now that the PageRank is added to Solr, we need an interface to interact and collect data, to compare the two ranking

algorithms. For that, we are going to write a PHP webpage which sends a request to Solr and utilizes and displays the search

results.

\1. Set the parameters of Lucene and PageRank algorithm:

lucene: fl: title,og\_url,og\_description,id

pageRank: fl: title,og\_url,og\_description,id; sort: pageRankFile desc

\2. Create a new solr service instance, we send request to **https://localhost**, **port: 8983** and **path: /solr/example1.**

\3. Get the response and show 4 fields in html: title(or NA), URL, description(or NA), id.





Search the Query

Now that we have everything setup, we can compare the ranking algorithms using the following queries.

**Queries**

Cannes

Congress

Democrats

Patriot Movement

Republicans

Senate

Olympics 2020

Stock

Virus

Fig. 4. Searching Cannes in Solr UI Dashboard

Analyzing and Comparing the Ranking Algorithms

Once the setup is completed, we can use the webpage we wrote to find out the search results for different queries. The following

pictures are screenshots of the search results for term “Cannes” in the search engine. We can also see that all the links shown

are redirected to their host site.





Fig. 5. Search result for "Cannes" using Lucene

Fig. 6. Search result for "Cannes" using PageRank





Fig. 7. Actual webpage from LATimes

Why some pages have higher PageRank values than others?

PageRank is mainly based on how many external links points to the webpage. The more external links point to the web page,

the more important the web page is. Thus, webpages with a larger number of external links that point to this page have a

higher PageRank values than others.

Query Search Results

Cannes

Lucene

PageRank

[https://www.latimes.com/local/obituaries/la-me-](https://www.latimes.com/local/obituaries/la-me-blake-edwards-20101217-story.html)

[blake-edwards-20101217-story.html](https://www.latimes.com/local/obituaries/la-me-blake-edwards-20101217-story.html)

<https://www.latimes.com/topic/cannes-film-festival>

[https://www.latimes.com/entertainment/movies/mo](https://www.latimes.com/entertainment/movies/moviesnow/la-et-mn-cannes-2015-winners-20150524-story.html)

[viesnow/la-et-mn-cannes-2015-winners-20150524-](https://www.latimes.com/entertainment/movies/moviesnow/la-et-mn-cannes-2015-winners-20150524-story.html)

[story.html](https://www.latimes.com/entertainment/movies/moviesnow/la-et-mn-cannes-2015-winners-20150524-story.html)

[https://www.latimes.com/local/california/la-me-](https://www.latimes.com/local/california/la-me-hugh-hefner-snap-story.html)

[hugh-hefner-snap-story.html](https://www.latimes.com/local/california/la-me-hugh-hefner-snap-story.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-wrap-critics-conversation-20190526-story.html)

[et-mn-cannes-wrap-critics-conversation-20190526-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-wrap-critics-conversation-20190526-story.html)

[story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-wrap-critics-conversation-20190526-story.html)

[https://www.latimes.com/archives/la-xpm-2003-](https://www.latimes.com/archives/la-xpm-2003-dec-31-me-dunne31-story.html)

[dec-31-me-dunne31-story.html](https://www.latimes.com/archives/la-xpm-2003-dec-31-me-dunne31-story.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-film-festival-award-winners-20190525-story.html)

[et-mn-cannes-film-festival-award-winners-20190525-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-film-festival-award-winners-20190525-story.html)

[story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-film-festival-award-winners-20190525-story.html)

[https://www.latimes.com/local/lanow/la-me-ln-](https://www.latimes.com/local/lanow/la-me-ln-bel-air-building-20170530-htmlstory.html)

[bel-air-building-20170530-htmlstory.html](https://www.latimes.com/local/lanow/la-me-ln-bel-air-building-20170530-htmlstory.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-parasite-atlantics-awards-20190526-story.html)[ ](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-parasite-atlantics-awards-20190526-story.html)[https://www.latimes.com/entertainment/envelope](https://www.latimes.com/entertainment/envelope/cotown/la-et-ct-morley-safer-dies-20160519-snap-story.html)

[et-mn-cannes-parasite-atlantics-awards-20190526-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-parasite-atlantics-awards-20190526-story.html)

[story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-parasite-atlantics-awards-20190526-story.html)

[/cotown/la-et-ct-morley-safer-dies-20160519-snap-](https://www.latimes.com/entertainment/envelope/cotown/la-et-ct-morley-safer-dies-20160519-snap-story.html)

[story.html](https://www.latimes.com/entertainment/envelope/cotown/la-et-ct-morley-safer-dies-20160519-snap-story.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-ca-mn-cannes-genre-parasite-wild-goose-lake-tarantino-20190522-story.html)

[ca-mn-cannes-genre-parasite-wild-goose-lake-](https://www.latimes.com/entertainment/movies/la-ca-mn-cannes-genre-parasite-wild-goose-lake-tarantino-20190522-story.html)

[tarantino-20190522-story.html](https://www.latimes.com/entertainment/movies/la-ca-mn-cannes-genre-parasite-wild-goose-lake-tarantino-20190522-story.html)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/story/2019-10-01/la-et-mg-prince-harry-meghan-suing-mail-associated-newspapers)

[arts/story/2019-10-01/la-et-mg-prince-harry-](https://www.latimes.com/entertainment-arts/story/2019-10-01/la-et-mg-prince-harry-meghan-suing-mail-associated-newspapers)

[meghan-suing-mail-associated-newspapers](https://www.latimes.com/entertainment-arts/story/2019-10-01/la-et-mg-prince-harry-meghan-suing-mail-associated-newspapers)





[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-once-upon-a-time-in-hollywood-20190522-story.html)

[et-mn-cannes-once-upon-a-time-in-hollywood-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-once-upon-a-time-in-hollywood-20190522-story.html)

[20190522-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-once-upon-a-time-in-hollywood-20190522-story.html)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/movies/story/2019-09-09/jojo-rabbit-knives-out-toronto-film-festival)

[arts/movies/story/2019-09-09/jojo-rabbit-knives-](https://www.latimes.com/entertainment-arts/movies/story/2019-09-09/jojo-rabbit-knives-out-toronto-film-festival)

[out-toronto-film-festival](https://www.latimes.com/entertainment-arts/movies/story/2019-09-09/jojo-rabbit-knives-out-toronto-film-festival)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-mektoub-my-love-intermezzo-abdellatif-kechiche-20190523-story.html)

[et-mn-cannes-mektoub-my-love-intermezzo-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-mektoub-my-love-intermezzo-abdellatif-kechiche-20190523-story.html)

[abdellatif-kechiche-20190523-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-mektoub-my-love-intermezzo-abdellatif-kechiche-20190523-story.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-quentin-tarantino-once-upon-a-time-in-hollywood-20190521-story.html)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/story/2019-08-05/asap-rocky-arrest-in-sweden-coverage)

[arts/story/2019-08-05/asap-rocky-arrest-in-](https://www.latimes.com/entertainment-arts/story/2019-08-05/asap-rocky-arrest-in-sweden-coverage)

[sweden-coverage](https://www.latimes.com/entertainment-arts/story/2019-08-05/asap-rocky-arrest-in-sweden-coverage)

[et-mn-cannes-quentin-tarantino-once-upon-a-time-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-quentin-tarantino-once-upon-a-time-in-hollywood-20190521-story.html)[ ](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-quentin-tarantino-once-upon-a-time-in-hollywood-20190521-story.html)[https://www.latimes.com/entertainment/envelope](https://www.latimes.com/entertainment/envelope/cotown/la-et-ct-ipic-lawsuit-20151117-story.html)

[in-hollywood-20190521-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-quentin-tarantino-once-upon-a-time-in-hollywood-20190521-story.html)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-robert-eggers-the-lighthouse-robert-pattinson-20190524-story.html)

[et-mn-cannes-robert-eggers-the-lighthouse-robert-](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-robert-eggers-the-lighthouse-robert-pattinson-20190524-story.html)

[pattinson-20190524-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-cannes-robert-eggers-the-lighthouse-robert-pattinson-20190524-story.html)

[/cotown/la-et-ct-ipic-lawsuit-20151117-story.html](https://www.latimes.com/entertainment/envelope/cotown/la-et-ct-ipic-lawsuit-20151117-story.html)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/movies/story/2019-10-24/lighthouse-explained-robert-pattinson-willem-dafoe)

[arts/movies/story/2019-10-24/lighthouse-](https://www.latimes.com/entertainment-arts/movies/story/2019-10-24/lighthouse-explained-robert-pattinson-willem-dafoe)

[explained-robert-pattinson-willem-dafoe](https://www.latimes.com/entertainment-arts/movies/story/2019-10-24/lighthouse-explained-robert-pattinson-willem-dafoe)

Congress

Lucene

PageRank

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-28/jerry-brown-to-testify-to-congress-rebutting-trumps-criticism-of-california)

[28/jerry-brown-to-testify-to-congress-rebutting-](https://www.latimes.com/politics/story/2019-10-28/jerry-brown-to-testify-to-congress-rebutting-trumps-criticism-of-california)

[trumps-criticism-of-california](https://www.latimes.com/politics/story/2019-10-28/jerry-brown-to-testify-to-congress-rebutting-trumps-criticism-of-california)

[https://www.latimes.com/obituaries/story/2019-](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

[07-18/john-tanton-dead-anti-immigrant](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

[https://www.latimes.com/opinion/enterthefray/la-](https://www.latimes.com/opinion/enterthefray/la-ol-new-york-rhode-island-congress-marijuana-20190116-story.html)

[ol-new-york-rhode-island-congress-marijuana-](https://www.latimes.com/opinion/enterthefray/la-ol-new-york-rhode-island-congress-marijuana-20190116-story.html)

[20190116-story.html](https://www.latimes.com/opinion/enterthefray/la-ol-new-york-rhode-island-congress-marijuana-20190116-story.html)

[https://www.latimes.com/politics/la-na-pol-trump-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[offshore-drilling-states-coastal-act-20190321-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[https://www.latimes.com/politics/la-pol-ca-richest-](https://www.latimes.com/politics/la-pol-ca-richest-california-lawmakers-20180305-story.html)

[california-lawmakers-20180305-story.html](https://www.latimes.com/politics/la-pol-ca-richest-california-lawmakers-20180305-story.html)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[26/whistleblower-complaint-against-president-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[trump-is-released](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-09/congress-gun-control-government-shutdown)

[09/congress-gun-control-government-shutdown](https://www.latimes.com/politics/story/2019-09-09/congress-gun-control-government-shutdown)

[https://www.latimes.com/politics/la-na-pol-](https://www.latimes.com/politics/la-na-pol-congress-sexual-harassment-20181212-story.html)

[congress-sexual-harassment-20181212-story.html](https://www.latimes.com/politics/la-na-pol-congress-sexual-harassment-20181212-story.html)

[https://www.latimes.com/opinion/story/2019-10-](https://www.latimes.com/opinion/story/2019-10-16/democratic-debate-trump-congress-executive-action)

[16/democratic-debate-trump-congress-executive-](https://www.latimes.com/opinion/story/2019-10-16/democratic-debate-trump-congress-executive-action)

[action](https://www.latimes.com/opinion/story/2019-10-16/democratic-debate-trump-congress-executive-action)

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

<https://www.latimes.com/sitemap/2018/6>

[https://www.latimes.com/nation/nationnow/la-na-](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[pittsburgh-synagogue-20181027-story.html](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[https://www.latimes.com/politics/la-na-pol-](https://www.latimes.com/politics/la-na-pol-congress-bioweapons-detection-system-20190414-story.html)

[congress-bioweapons-detection-system-20190414-](https://www.latimes.com/politics/la-na-pol-congress-bioweapons-detection-system-20190414-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-congress-bioweapons-detection-system-20190414-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-14/trump-ukraine-aid-congress-impeachment)

[14/trump-ukraine-aid-congress-impeachment](https://www.latimes.com/politics/story/2019-10-14/trump-ukraine-aid-congress-impeachment)

[https://www.latimes.com/politics/la-pol-ca-richest-](https://www.latimes.com/politics/la-pol-ca-richest-in-congress-darrell-issa-story.html)

[in-congress-darrell-issa-story.html](https://www.latimes.com/politics/la-pol-ca-richest-in-congress-darrell-issa-story.html)

[https://www.latimes.com/california/story/2019-10-](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[21/california-independent-voters-can-cast-ballots-](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[for-democrats-not-trump-march-2020-primary](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[https://www.latimes.com/business/story/2019-09-](https://www.latimes.com/business/story/2019-09-02/debt-collection-cfpb)

[02/debt-collection-cfpb](https://www.latimes.com/business/story/2019-09-02/debt-collection-cfpb)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[23/trump-gun-laws-background-checks-2020-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[election](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[https://www.latimes.com/politics/la-pol-ca-richest-](https://www.latimes.com/politics/la-pol-ca-richest-california-lawmakers-20180305-story.html)

[california-lawmakers-20180305-story.html](https://www.latimes.com/politics/la-pol-ca-richest-california-lawmakers-20180305-story.html)

[https://www.latimes.com/business/lazarus/la-fi-](https://www.latimes.com/business/lazarus/la-fi-lazarus-marketers-fight-california-privacy-law-20180821-story.html)

[lazarus-marketers-fight-california-privacy-law-](https://www.latimes.com/business/lazarus/la-fi-lazarus-marketers-fight-california-privacy-law-20180821-story.html)

[20180821-story.html](https://www.latimes.com/business/lazarus/la-fi-lazarus-marketers-fight-california-privacy-law-20180821-story.html)





Democrats

Lucene

PageRank

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-07/house-democrats-subpoena-mick-mulvaney-in-impeachment-probe)

[07/house-democrats-subpoena-mick-mulvaney-in-](https://www.latimes.com/politics/story/2019-11-07/house-democrats-subpoena-mick-mulvaney-in-impeachment-probe)

[impeachment-probe](https://www.latimes.com/politics/story/2019-11-07/house-democrats-subpoena-mick-mulvaney-in-impeachment-probe)

[https://www.latimes.com/nation/politics/la-na-pol-](https://www.latimes.com/nation/politics/la-na-pol-williamson-vaccines-20190619-story.html)

[williamson-vaccines-20190619-story.html](https://www.latimes.com/nation/politics/la-na-pol-williamson-vaccines-20190619-story.html)

[https://www.latimes.com/opinion/story/2019-11-](https://www.latimes.com/opinion/story/2019-11-05/impeachment-donald-trump-democrats-political-cost-elections)

[05/impeachment-donald-trump-democrats-political-](https://www.latimes.com/opinion/story/2019-11-05/impeachment-donald-trump-democrats-political-cost-elections)

[cost-elections](https://www.latimes.com/opinion/story/2019-11-05/impeachment-donald-trump-democrats-political-cost-elections)

[https://www.latimes.com/nation/la-na-pol-trump-](https://www.latimes.com/nation/la-na-pol-trump-charity-new-york-suit-20180614-story.html)

[charity-new-york-suit-20180614-story.html](https://www.latimes.com/nation/la-na-pol-trump-charity-new-york-suit-20180614-story.html)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-29/trump-allies-and-democrats-reveal-the-deep-divisions-over-impeachment-inquiry)

[29/trump-allies-and-democrats-reveal-the-deep-](https://www.latimes.com/politics/story/2019-09-29/trump-allies-and-democrats-reveal-the-deep-divisions-over-impeachment-inquiry)

[divisions-over-impeachment-inquiry](https://www.latimes.com/politics/story/2019-09-29/trump-allies-and-democrats-reveal-the-deep-divisions-over-impeachment-inquiry)

[https://www.latimes.com/world-nation/story/2019-](https://www.latimes.com/world-nation/story/2019-11-06/las-vegas-street-sleeping-law)

[11-06/las-vegas-street-sleeping-law](https://www.latimes.com/world-nation/story/2019-11-06/las-vegas-street-sleeping-law)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-24/democrats-and-pelosi-appear-close-to-tipping-point-on-impeachment)

[24/democrats-and-pelosi-appear-close-to-tipping-](https://www.latimes.com/politics/story/2019-09-24/democrats-and-pelosi-appear-close-to-tipping-point-on-impeachment)

[point-on-impeachment](https://www.latimes.com/politics/story/2019-09-24/democrats-and-pelosi-appear-close-to-tipping-point-on-impeachment)

[https://www.latimes.com/politics/la-na-pol-trump-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[offshore-drilling-states-coastal-act-20190321-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-17/democrats-quick-impeachment-timing-complicated)

[17/democrats-quick-impeachment-timing-](https://www.latimes.com/politics/story/2019-10-17/democrats-quick-impeachment-timing-complicated)

[complicated](https://www.latimes.com/politics/story/2019-10-17/democrats-quick-impeachment-timing-complicated)

<https://www.latimes.com/people/matt-pearce>

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-08/impeachment-trump-democrats-whistleblower-identity-testimony)

[08/impeachment-trump-democrats-whistleblower-](https://www.latimes.com/politics/story/2019-10-08/impeachment-trump-democrats-whistleblower-identity-testimony)

[identity-testimony](https://www.latimes.com/politics/story/2019-10-08/impeachment-trump-democrats-whistleblower-identity-testimony)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[26/whistleblower-complaint-against-president-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[trump-is-released](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-03/iowa-democrats-candidate-beat-trump)

[03/iowa-democrats-candidate-beat-trump](https://www.latimes.com/politics/story/2019-11-03/iowa-democrats-candidate-beat-trump)

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-07/democrats-to-build-abuse-of-power-case-against-trump-next-week)

[07/democrats-to-build-abuse-of-power-case-](https://www.latimes.com/politics/story/2019-11-07/democrats-to-build-abuse-of-power-case-against-trump-next-week)

[against-trump-next-week](https://www.latimes.com/politics/story/2019-11-07/democrats-to-build-abuse-of-power-case-against-trump-next-week)

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

<https://www.latimes.com/sitemap/2018/6>

[https://www.latimes.com/nation/ct-democrats-](https://www.latimes.com/nation/ct-democrats-economic-plan-20170824-story.html)

[economic-plan-20170824-story.html](https://www.latimes.com/nation/ct-democrats-economic-plan-20170824-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-03/house-democrats-in-trump-districts-have-backing-from-voters)

[03/house-democrats-in-trump-districts-have-](https://www.latimes.com/politics/story/2019-10-03/house-democrats-in-trump-districts-have-backing-from-voters)

[backing-from-voters](https://www.latimes.com/politics/story/2019-10-03/house-democrats-in-trump-districts-have-backing-from-voters)

[https://www.latimes.com/nation/nationnow/la-na-](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[pittsburgh-synagogue-20181027-story.html](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[https://www.latimes.com/entertainment/arts/la-et-](https://www.latimes.com/entertainment/arts/la-et-cm-laurie-metcalf-hillary-clinton-20190430-story.html)

[cm-laurie-metcalf-hillary-clinton-20190430-](https://www.latimes.com/entertainment/arts/la-et-cm-laurie-metcalf-hillary-clinton-20190430-story.html)

[story.html](https://www.latimes.com/entertainment/arts/la-et-cm-laurie-metcalf-hillary-clinton-20190430-story.html)

Patriot Movement

Lucene

PageRank

[https://www.latimes.com/archives/la-xpm-2012-oct-](https://www.latimes.com/archives/la-xpm-2012-oct-23-la-me-russell-means-20121023-story.html)

[23-la-me-russell-means-20121023-story.html](https://www.latimes.com/archives/la-xpm-2012-oct-23-la-me-russell-means-20121023-story.html)

[https://www.latimes.com/local/obituaries/la-me-](https://www.latimes.com/local/obituaries/la-me-blake-edwards-20101217-story.html)

[blake-edwards-20101217-story.html](https://www.latimes.com/local/obituaries/la-me-blake-edwards-20101217-story.html)

[https://www.latimes.com/sports/nba/la-sp-nba-best-](https://www.latimes.com/sports/nba/la-sp-nba-best-game-ever-20181222-story.html)[ ](https://www.latimes.com/sports/nba/la-sp-nba-best-game-ever-20181222-story.html)[https://www.latimes.com/obituaries/story/2019-](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

[game-ever-20181222-story.html](https://www.latimes.com/sports/nba/la-sp-nba-best-game-ever-20181222-story.html)

[https://www.latimes.com/archives/la-xpm-2001-jun-](https://www.latimes.com/archives/la-xpm-2001-jun-10-mn-8792-story.html)

[10-mn-8792-story.html](https://www.latimes.com/archives/la-xpm-2001-jun-10-mn-8792-story.html)

[07-18/john-tanton-dead-anti-immigrant](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

[https://www.latimes.com/local/lanow/la-me-ln-](https://www.latimes.com/local/lanow/la-me-ln-710-fwy-extension-alternative-20181129-story.html)

[710-fwy-extension-alternative-20181129-](https://www.latimes.com/local/lanow/la-me-ln-710-fwy-extension-alternative-20181129-story.html)

[story.html](https://www.latimes.com/local/lanow/la-me-ln-710-fwy-extension-alternative-20181129-story.html)

<https://www.latimes.com/staff/megan-garvey>

[https://www.latimes.com/nation/nationnow/la-na-](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[pittsburgh-synagogue-20181027-story.html](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[https://www.latimes.com/local/california/la-me-](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[nation-divided-in-huntington-beach-parade-](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[20190704-story.html](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/books/story/2019-10-25/shadowlands-anthony-mccann-oregon-standoff)

[arts/books/story/2019-10-25/shadowlands-anthony-](https://www.latimes.com/entertainment-arts/books/story/2019-10-25/shadowlands-anthony-mccann-oregon-standoff)

[mccann-oregon-standoff](https://www.latimes.com/entertainment-arts/books/story/2019-10-25/shadowlands-anthony-mccann-oregon-standoff)





[https://www.latimes.com/sports/highschool/story/20](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[19-11-06/girls-tennis-southern-section-playoff-](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[results-and-updated-pairings](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/story/2019-09-04/placido-domingo-new-sexual-harassment-allegations)

[arts/story/2019-09-04/placido-domingo-new-](https://www.latimes.com/entertainment-arts/story/2019-09-04/placido-domingo-new-sexual-harassment-allegations)

[sexual-harassment-allegations](https://www.latimes.com/entertainment-arts/story/2019-09-04/placido-domingo-new-sexual-harassment-allegations)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)[ ](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)[https://www.latimes.com/entertainment/la-et-mn-](https://www.latimes.com/entertainment/la-et-mn-rose-mcgowan-drug-charge-no-contest-20190109-story.html)

[et-mn-july-4-box-office-history-20180629-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)[ ](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)[rose-mcgowan-drug-charge-no-contest-20190109-](https://www.latimes.com/entertainment/la-et-mn-rose-mcgowan-drug-charge-no-contest-20190109-story.html)

[story.html](https://www.latimes.com/entertainment/la-et-mn-rose-mcgowan-drug-charge-no-contest-20190109-story.html)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-20/bernie-sanders-muslim-voters-2020)

[20/bernie-sanders-muslim-voters-2020](https://www.latimes.com/politics/story/2019-09-20/bernie-sanders-muslim-voters-2020)

[https://www.latimes.com/california/story/2019-](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

[10-21/affordable-housing-activists-push-back-real-](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

[estate-capitalism](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

[https://www.latimes.com/entertainment/movies/la-](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)

[et-mn-july-4-box-office-history-20180629-story.html](https://www.latimes.com/entertainment/movies/la-et-mn-july-4-box-office-history-20180629-story.html)

[https://www.latimes.com/politics/la-na-pol-](https://www.latimes.com/politics/la-na-pol-pentagon-confirmation-vacancies-20190716-story.html)

[pentagon-confirmation-vacancies-20190716-](https://www.latimes.com/politics/la-na-pol-pentagon-confirmation-vacancies-20190716-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-pentagon-confirmation-vacancies-20190716-story.html)

[https://www.latimes.com/sports/highschool/story/20](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[19-11-06/girls-tennis-southern-section-playoff-](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[results-and-updated-pairings](https://www.latimes.com/sports/highschool/story/2019-11-06/girls-tennis-southern-section-playoff-results-and-updated-pairings)

[https://www.latimes.com/world-](https://www.latimes.com/world-nation/story/2019-07-25/in-puerto-rico-calm-after-rossello-ouster)

[nation/story/2019-07-25/in-puerto-rico-calm-after-](https://www.latimes.com/world-nation/story/2019-07-25/in-puerto-rico-calm-after-rossello-ouster)

[rossello-ouster](https://www.latimes.com/world-nation/story/2019-07-25/in-puerto-rico-calm-after-rossello-ouster)

Republicans

Lucene

PageRank

[https://www.latimes.com/opinion/story/2019-11-](https://www.latimes.com/opinion/story/2019-11-08/impeachment-trump-republicans-toomey)

[08/impeachment-trump-republicans-toomey](https://www.latimes.com/opinion/story/2019-11-08/impeachment-trump-republicans-toomey)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[18/opinion-arizona-appeals-court-executions-](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[secrecy-death-penalty](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[https://www.latimes.com/opinion/story/2019-10-](https://www.latimes.com/opinion/story/2019-10-23/house-republicans-storm-hearing-impeachment)

[23/house-republicans-storm-hearing-impeachment](https://www.latimes.com/opinion/story/2019-10-23/house-republicans-storm-hearing-impeachment)

[https://www.latimes.com/politics/la-na-pol-trump-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[offshore-drilling-states-coastal-act-20190321-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-30/democrats-hoped-theyd-win-over-republicans-on-impeachment-but-its-not-looking-that-way-so-far)

[30/democrats-hoped-theyd-win-over-republicans-](https://www.latimes.com/politics/story/2019-10-30/democrats-hoped-theyd-win-over-republicans-on-impeachment-but-its-not-looking-that-way-so-far)

[on-impeachment-but-its-not-looking-that-way-so-far](https://www.latimes.com/politics/story/2019-10-30/democrats-hoped-theyd-win-over-republicans-on-impeachment-but-its-not-looking-that-way-so-far)

[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[26/whistleblower-complaint-against-president-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[trump-is-released](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[https://www.latimes.com/california/story/2019-10-](https://www.latimes.com/california/story/2019-10-16/california-republicans-democrats-wildfires-homelessness)[ ](https://www.latimes.com/california/story/2019-10-16/california-republicans-democrats-wildfires-homelessness)[https://www.latimes.com/opinion/livable-city/la-ol-](https://www.latimes.com/opinion/livable-city/la-ol-plastic-pollution-styrofoam-20170529-story.html)

[16/california-republicans-democrats-wildfires-](https://www.latimes.com/california/story/2019-10-16/california-republicans-democrats-wildfires-homelessness)

[homelessness](https://www.latimes.com/california/story/2019-10-16/california-republicans-democrats-wildfires-homelessness)

[plastic-pollution-styrofoam-20170529-story.html](https://www.latimes.com/opinion/livable-city/la-ol-plastic-pollution-styrofoam-20170529-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-04/vulnerable-senate-republicans-impeachment)

[04/vulnerable-senate-republicans-impeachment](https://www.latimes.com/politics/story/2019-10-04/vulnerable-senate-republicans-impeachment)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-23/impeachment-deposition-room-stormed-by-republicans)

[23/impeachment-deposition-room-stormed-by-](https://www.latimes.com/politics/story/2019-10-23/impeachment-deposition-room-stormed-by-republicans)

[republicans](https://www.latimes.com/politics/story/2019-10-23/impeachment-deposition-room-stormed-by-republicans)

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

<https://www.latimes.com/sitemap/2018/6>

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-06/republicans-election-trump-candidate-loses-kentucky)

[06/republicans-election-trump-candidate-loses-](https://www.latimes.com/politics/story/2019-11-06/republicans-election-trump-candidate-loses-kentucky)

[kentucky](https://www.latimes.com/politics/story/2019-11-06/republicans-election-trump-candidate-loses-kentucky)

[https://www.latimes.com/local/california/la-me-](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[nation-divided-in-huntington-beach-parade-](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[20190704-story.html](https://www.latimes.com/local/california/la-me-nation-divided-in-huntington-beach-parade-20190704-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-28/katie-hill-resignation-trump-obstacle-for-republicans)

[28/katie-hill-resignation-trump-obstacle-for-](https://www.latimes.com/politics/story/2019-10-28/katie-hill-resignation-trump-obstacle-for-republicans)

[republicans](https://www.latimes.com/politics/story/2019-10-28/katie-hill-resignation-trump-obstacle-for-republicans)

[https://www.latimes.com/opinion/livable-city/la-](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[oe-fraser-chester-matute-parking-20161215-](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[story.html](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-17/california-republicans-trump-fundraising-president)

[17/california-republicans-trump-fundraising-](https://www.latimes.com/opinion/story/2019-09-17/california-republicans-trump-fundraising-president)

[president](https://www.latimes.com/opinion/story/2019-09-17/california-republicans-trump-fundraising-president)

[https://www.latimes.com/opinion/story/2019-10-](https://www.latimes.com/opinion/story/2019-10-09/patt-morrison-sarah-longwell-never-trump-impeachment)

[09/patt-morrison-sarah-longwell-never-trump-](https://www.latimes.com/opinion/story/2019-10-09/patt-morrison-sarah-longwell-never-trump-impeachment)

[impeachment](https://www.latimes.com/opinion/story/2019-10-09/patt-morrison-sarah-longwell-never-trump-impeachment)

[https://www.latimes.com/california/story/2019-10-](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[21/california-independent-voters-can-cast-ballots-](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[for-democrats-not-trump-march-2020-primary](https://www.latimes.com/california/story/2019-10-21/california-independent-voters-can-cast-ballots-for-democrats-not-trump-march-2020-primary)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[23/trump-gun-laws-background-checks-2020-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[election](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)





Senate

Lucene

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)

PageRank

[https://www.latimes.com/sports/lakers/story/2019](https://www.latimes.com/sports/lakers/story/2019-09-30/lebron-james-ncaa-reform-sb206-california-lakers)

[30/california-donors-spend-millions-on-2020-senate-](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)[ ](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)[-09-30/lebron-james-ncaa-reform-sb206-california-](https://www.latimes.com/sports/lakers/story/2019-09-30/lebron-james-ncaa-reform-sb206-california-lakers)

[races-across-the-country](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)

[lakers](https://www.latimes.com/sports/lakers/story/2019-09-30/lebron-james-ncaa-reform-sb206-california-lakers)

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[07/gop-is-already-thinking-about-how-to-turn-a-](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[senate-impeachment-trial-to-trumps-advantage](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-04/vulnerable-senate-republicans-impeachment)

[04/vulnerable-senate-republicans-impeachment](https://www.latimes.com/politics/story/2019-10-04/vulnerable-senate-republicans-impeachment)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[18/opinion-arizona-appeals-court-executions-](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[secrecy-death-penalty](https://www.latimes.com/opinion/story/2019-09-18/opinion-arizona-appeals-court-executions-secrecy-death-penalty)

[https://www.latimes.com/politics/la-na-pol-trump-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[offshore-drilling-states-coastal-act-20190321-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[https://www.latimes.com/politics/story/2019-08-](https://www.latimes.com/politics/story/2019-08-14/2020-senate-control-presidential-race)

[14/2020-senate-control-presidential-race](https://www.latimes.com/politics/story/2019-08-14/2020-senate-control-presidential-race)

[https://www.latimes.com/politics/story/2019-08-](https://www.latimes.com/politics/story/2019-08-14/john-hickenlooper-quits-presidential-race-for-senate-run)

[14/john-hickenlooper-quits-presidential-race-for-](https://www.latimes.com/politics/story/2019-08-14/john-hickenlooper-quits-presidential-race-for-senate-run)

[senate-run](https://www.latimes.com/politics/story/2019-08-14/john-hickenlooper-quits-presidential-race-for-senate-run)

[https://www.latimes.com/opinion/livable-city/la-ol-](https://www.latimes.com/opinion/livable-city/la-ol-plastic-pollution-styrofoam-20170529-story.html)

[plastic-pollution-styrofoam-20170529-story.html](https://www.latimes.com/opinion/livable-city/la-ol-plastic-pollution-styrofoam-20170529-story.html)

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[https://www.latimes.com/politics/la-na-pol-](https://www.latimes.com/politics/la-na-pol-kavanaugh-hearing-20180927-story.html)

[kavanaugh-hearing-20180927-story.html](https://www.latimes.com/politics/la-na-pol-kavanaugh-hearing-20180927-story.html)

[https://www.latimes.com/nation/la-pol-scotus-](https://www.latimes.com/nation/la-pol-scotus-confirmation-votes-over-the-years-20181005-htmlstory.html)

[confirmation-votes-over-the-years-20181005-](https://www.latimes.com/nation/la-pol-scotus-confirmation-votes-over-the-years-20181005-htmlstory.html)

[htmlstory.html](https://www.latimes.com/nation/la-pol-scotus-confirmation-votes-over-the-years-20181005-htmlstory.html)

<https://www.latimes.com/sitemap/2018/6>

[https://www.latimes.com/local/lanow/la-me-](https://www.latimes.com/local/lanow/la-me-police-records-california-20190630-story.html)

[police-records-california-20190630-story.html](https://www.latimes.com/local/lanow/la-me-police-records-california-20190630-story.html)

[https://www.latimes.com/politics/la-na-pol-william-](https://www.latimes.com/politics/la-na-pol-william-barr-senate-confirm-attorney-general-20190214-story.html)

[barr-senate-confirm-attorney-general-20190214-](https://www.latimes.com/politics/la-na-pol-william-barr-senate-confirm-attorney-general-20190214-story.html)

[story.html](https://www.latimes.com/politics/la-na-pol-william-barr-senate-confirm-attorney-general-20190214-story.html)

[https://www.latimes.com/opinion/livable-city/la-](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[oe-fraser-chester-matute-parking-20161215-](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[story.html](https://www.latimes.com/opinion/livable-city/la-oe-fraser-chester-matute-parking-20161215-story.html)

[https://www.latimes.com/politics/story/2019-10-](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)

[30/california-donors-spend-millions-on-2020-senate-](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)

[races-across-the-country](https://www.latimes.com/politics/story/2019-10-30/california-donors-spend-millions-on-2020-senate-races-across-the-country)

[https://www.latimes.com/nation/la-na-pol-us-](https://www.latimes.com/nation/la-na-pol-us-pacific-20180629-story.html)

[pacific-20180629-story.html](https://www.latimes.com/nation/la-na-pol-us-pacific-20180629-story.html)

[https://www.latimes.com/politics/story/2019-11-](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[07/gop-is-already-thinking-about-how-to-turn-a-](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[senate-impeachment-trial-to-trumps-advantage](https://www.latimes.com/politics/story/2019-11-07/gop-is-already-thinking-about-how-to-turn-a-senate-impeachment-trial-to-trumps-advantage)

[https://www.latimes.com/opinion/story/2019-09-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[23/trump-gun-laws-background-checks-2020-](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

[election](https://www.latimes.com/opinion/story/2019-09-23/trump-gun-laws-background-checks-2020-election)

Olympics 2020

Lucene

PageRank

[https://www.latimes.com/sports/olympics/story/2019](https://www.latimes.com/sports/olympics/story/2019-08-15/2020-tokyo-olympics-searching-for-answers-amid-heat-wave)

[-08-15/2020-tokyo-olympics-searching-for-answers-](https://www.latimes.com/sports/olympics/story/2019-08-15/2020-tokyo-olympics-searching-for-answers-amid-heat-wave)

[amid-heat-wave](https://www.latimes.com/sports/olympics/story/2019-08-15/2020-tokyo-olympics-searching-for-answers-amid-heat-wave)

[https://www.latimes.com/nation/politics/la-na-](https://www.latimes.com/nation/politics/la-na-pol-williamson-vaccines-20190619-story.html)

[pol-williamson-vaccines-20190619-story.html](https://www.latimes.com/nation/politics/la-na-pol-williamson-vaccines-20190619-story.html)

<https://www.latimes.com/sports/olympics>

[https://www.latimes.com/socal/la-canada-valley-](https://www.latimes.com/socal/la-canada-valley-sun/news/story/2019-11-05/man-brandishes-firearm-flint-canyon-tennis-club-leads-deputies-search)

[sun/news/story/2019-11-05/man-brandishes-](https://www.latimes.com/socal/la-canada-valley-sun/news/story/2019-11-05/man-brandishes-firearm-flint-canyon-tennis-club-leads-deputies-search)

[firearm-flint-canyon-tennis-club-leads-deputies-](https://www.latimes.com/socal/la-canada-valley-sun/news/story/2019-11-05/man-brandishes-firearm-flint-canyon-tennis-club-leads-deputies-search)

[search](https://www.latimes.com/socal/la-canada-valley-sun/news/story/2019-11-05/man-brandishes-firearm-flint-canyon-tennis-club-leads-deputies-search)

[https://www.latimes.com/sports/olympics/story/2019](https://www.latimes.com/sports/olympics/story/2019-10-16/2020-tokyo-olympics-marathon-sapporo-avoid-heat)

[-10-16/2020-tokyo-olympics-marathon-sapporo-](https://www.latimes.com/sports/olympics/story/2019-10-16/2020-tokyo-olympics-marathon-sapporo-avoid-heat)

[avoid-heat](https://www.latimes.com/sports/olympics/story/2019-10-16/2020-tokyo-olympics-marathon-sapporo-avoid-heat)

[https://www.latimes.com/entertainment-](https://www.latimes.com/entertainment-arts/business/story/2019-08-23/disney-pitches-its-upcoming-streaming-service-disney-to-its-biggest-fans)

[arts/business/story/2019-08-23/disney-pitches-its-](https://www.latimes.com/entertainment-arts/business/story/2019-08-23/disney-pitches-its-upcoming-streaming-service-disney-to-its-biggest-fans)

[upcoming-streaming-service-disney-to-its-biggest-](https://www.latimes.com/entertainment-arts/business/story/2019-08-23/disney-pitches-its-upcoming-streaming-service-disney-to-its-biggest-fans)

[fans](https://www.latimes.com/entertainment-arts/business/story/2019-08-23/disney-pitches-its-upcoming-streaming-service-disney-to-its-biggest-fans)





[https://www.latimes.com/sports/olympics/story/2019](https://www.latimes.com/sports/olympics/story/2019-07-31/deadly-heat-wave-2020-summer-olympics-tokyo)[ ](https://www.latimes.com/sports/olympics/story/2019-07-31/deadly-heat-wave-2020-summer-olympics-tokyo)[https://www.latimes.com/obituaries/story/2019-](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

[-07-31/deadly-heat-wave-2020-summer-olympics-](https://www.latimes.com/sports/olympics/story/2019-07-31/deadly-heat-wave-2020-summer-olympics-tokyo)

[tokyo](https://www.latimes.com/sports/olympics/story/2019-07-31/deadly-heat-wave-2020-summer-olympics-tokyo)

[07-18/john-tanton-dead-anti-immigrant](https://www.latimes.com/obituaries/story/2019-07-18/john-tanton-dead-anti-immigrant)

<https://www.latimes.com/people/david-wharton>

[https://www.latimes.com/politics/la-na-pol-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[trump-offshore-drilling-states-coastal-act-](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[20190321-story.html](https://www.latimes.com/politics/la-na-pol-trump-offshore-drilling-states-coastal-act-20190321-story.html)

[https://www.latimes.com/sports/story/2019-11-](https://www.latimes.com/sports/story/2019-11-05/ioc-anti-doping-2020-tokyo-olympics)

[05/ioc-anti-doping-2020-tokyo-olympics](https://www.latimes.com/sports/story/2019-11-05/ioc-anti-doping-2020-tokyo-olympics)

<https://www.latimes.com/people/matt-pearce>

[https://www.latimes.com/sports/olympics/story/2019](https://www.latimes.com/sports/olympics/story/2019-10-10/naomi-osaka-chooses-japan-over-u-s-2020-tokyo-olympics)[ ](https://www.latimes.com/sports/olympics/story/2019-10-10/naomi-osaka-chooses-japan-over-u-s-2020-tokyo-olympics)[https://www.latimes.com/politics/story/2019-09-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[-10-10/naomi-osaka-chooses-japan-over-u-s-2020-](https://www.latimes.com/sports/olympics/story/2019-10-10/naomi-osaka-chooses-japan-over-u-s-2020-tokyo-olympics)

[tokyo-olympics](https://www.latimes.com/sports/olympics/story/2019-10-10/naomi-osaka-chooses-japan-over-u-s-2020-tokyo-olympics)

[26/whistleblower-complaint-against-president-](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

[trump-is-released](https://www.latimes.com/politics/story/2019-09-26/whistleblower-complaint-against-president-trump-is-released)

<https://www.latimes.com/people/david-wharton>

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

<https://www.latimes.com/sitemap/2018/6>

[https://www.latimes.com/sports/story/2019-07-](https://www.latimes.com/sports/story/2019-07-23/tokyos-rough-road-to-2020-summer-olympics)

[23/tokyos-rough-road-to-2020-summer-olympics](https://www.latimes.com/sports/story/2019-07-23/tokyos-rough-road-to-2020-summer-olympics)

<https://www.latimes.com/sports/olympics>

[https://www.latimes.com/nation/nationnow/la-](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

[na-pittsburgh-synagogue-20181027-story.html](https://www.latimes.com/nation/nationnow/la-na-pittsburgh-synagogue-20181027-story.html)

Stock

Lucene

PageRank

[https://www.latimes.com/business/story/2019-11-](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[06/uber-lock-up-period-ends-with-falling-stock-](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[protests](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[https://www.latimes.com/business/story/2019-11-](https://www.latimes.com/business/story/2019-11-08/netflix-hbo-password-sharing)

[08/netflix-hbo-password-sharing](https://www.latimes.com/business/story/2019-11-08/netflix-hbo-password-sharing)

[https://www.latimes.com/business/la-fi-lyft-stock-](https://www.latimes.com/business/la-fi-lyft-stock-20190401-story.html)

[20190401-story.html](https://www.latimes.com/business/la-fi-lyft-stock-20190401-story.html)

[https://www.latimes.com/socal/daily-](https://www.latimes.com/socal/daily-pilot/news/story/2019-11-07/small-fish-in-a-big-pond-2-000-young-white-sea-bass-help-enhance-marine-population-by-way-of-newport-beach)

[pilot/news/story/2019-11-07/small-fish-in-a-big-](https://www.latimes.com/socal/daily-pilot/news/story/2019-11-07/small-fish-in-a-big-pond-2-000-young-white-sea-bass-help-enhance-marine-population-by-way-of-newport-beach)

[pond-2-000-young-white-sea-bass-help-enhance-](https://www.latimes.com/socal/daily-pilot/news/story/2019-11-07/small-fish-in-a-big-pond-2-000-young-white-sea-bass-help-enhance-marine-population-by-way-of-newport-beach)

[marine-population-by-way-of-newport-beach](https://www.latimes.com/socal/daily-pilot/news/story/2019-11-07/small-fish-in-a-big-pond-2-000-young-white-sea-bass-help-enhance-marine-population-by-way-of-newport-beach)

[https://www.latimes.com/business/story/2019-10-](https://www.latimes.com/business/story/2019-10-29/dutch-chocolate-shop-la-historic-cultural-monument)

[29/dutch-chocolate-shop-la-historic-cultural-](https://www.latimes.com/business/story/2019-10-29/dutch-chocolate-shop-la-historic-cultural-monument)

[monument](https://www.latimes.com/business/story/2019-10-29/dutch-chocolate-shop-la-historic-cultural-monument)

[https://www.latimes.com/business/la-fi-hy-tesla-](https://www.latimes.com/business/la-fi-hy-tesla-stock-20160518-snap-story.html)

[stock-20160518-snap-story.html](https://www.latimes.com/business/la-fi-hy-tesla-stock-20160518-snap-story.html)

[https://www.latimes.com/business/story/2019-11-](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[09/stock-managers-who-played-defense-in-2019-are-](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[left-scrambling-to-make-up-ground](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[https://www.latimes.com/business/story/2019-08-](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[30/ab5-dynamex-independent-contractors-bill](https://www.latimes.com/business/story/2019-08-30/ab5-dynamex-independent-contractors-bill)

[https://www.latimes.com/business/story/2019-11-](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[06/uber-lock-up-period-ends-with-falling-stock-](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[protests](https://www.latimes.com/business/story/2019-11-06/uber-lock-up-period-ends-with-falling-stock-protests)

[https://www.latimes.com/business/story/2019-10-](https://www.latimes.com/business/story/2019-10-28/california-fires-private-enterprise)

[28/california-fires-private-enterprise](https://www.latimes.com/business/story/2019-10-28/california-fires-private-enterprise)

[https://www.latimes.com/business/la-fi-uber-ipo-](https://www.latimes.com/business/la-fi-uber-ipo-stock-trading-price-20190510-story.html)

[stock-trading-price-20190510-story.html](https://www.latimes.com/business/la-fi-uber-ipo-stock-trading-price-20190510-story.html)

[https://www.latimes.com/archives/la-xpm-2000-](https://www.latimes.com/archives/la-xpm-2000-apr-23-mn-22581-story.html)

[apr-23-mn-22581-story.html](https://www.latimes.com/archives/la-xpm-2000-apr-23-mn-22581-story.html)

[https://www.latimes.com/business/story/2019-11-](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[09/stock-managers-who-played-defense-in-2019-are-](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[left-scrambling-to-make-up-ground](https://www.latimes.com/business/story/2019-11-09/stock-managers-who-played-defense-in-2019-are-left-scrambling-to-make-up-ground)

[https://www.latimes.com/business/real-](https://www.latimes.com/business/real-estate/story/2019-11-06/golfer-fred-couples-newport-beach-home-for-sale)

[estate/story/2019-11-06/golfer-fred-couples-](https://www.latimes.com/business/real-estate/story/2019-11-06/golfer-fred-couples-newport-beach-home-for-sale)

[newport-beach-home-for-sale](https://www.latimes.com/business/real-estate/story/2019-11-06/golfer-fred-couples-newport-beach-home-for-sale)

[https://www.latimes.com/business/la-fi-stock-](https://www.latimes.com/business/la-fi-stock-market-status-update-20180209-story.html)

[market-status-update-20180209-story.html](https://www.latimes.com/business/la-fi-stock-market-status-update-20180209-story.html)

[https://www.latimes.com/business/story/2019-09-](https://www.latimes.com/business/story/2019-09-09/at-t-stock-surges-elliott-reveals-3-2-billion-stake)

[09/at-t-stock-surges-elliott-reveals-3-2-billion-stake](https://www.latimes.com/business/story/2019-09-09/at-t-stock-surges-elliott-reveals-3-2-billion-stake)

[https://www.latimes.com/business/story/2019-09-](https://www.latimes.com/business/story/2019-09-02/debt-collection-cfpb)

[02/debt-collection-cfpb](https://www.latimes.com/business/story/2019-09-02/debt-collection-cfpb)

[https://www.latimes.com/business/la-fi-mattel-](https://www.latimes.com/business/la-fi-mattel-mga-merger-offer-20190611-story.html)

[mga-merger-offer-20190611-story.html](https://www.latimes.com/business/la-fi-mattel-mga-merger-offer-20190611-story.html)





[https://www.latimes.com/business/la-fi-stock-](https://www.latimes.com/business/la-fi-stock-market-cpi-roundup-20180214-story.html)

[market-cpi-roundup-20180214-story.html](https://www.latimes.com/business/la-fi-stock-market-cpi-roundup-20180214-story.html)

[https://www.latimes.com/california/story/2019-10-](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

[21/affordable-housing-activists-push-back-real-](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

[estate-capitalism](https://www.latimes.com/california/story/2019-10-21/affordable-housing-activists-push-back-real-estate-capitalism)

Virus

Lucene

PageRank

[https://www.latimes.com/espanol/internacional/art](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[ ](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[https://www.latimes.com/espanol/vidayestilo/la-es-](https://www.latimes.com/espanol/vidayestilo/la-es-como-evitar-ser-estafado-en-medio-de-todas-esas-buenas-ofertas-del-viernes-negro-20181121-story.html)

[iculo/2019-11-09/monos-se-volvieron-inmunes-tras-](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[ ](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[como-evitar-ser-estafado-en-medio-de-todas-esas-](https://www.latimes.com/espanol/vidayestilo/la-es-como-evitar-ser-estafado-en-medio-de-todas-esas-buenas-ofertas-del-viernes-negro-20181121-story.html)

[inocularles-virus-del-ebola-con-mutacion](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[buenas-ofertas-del-viernes-negro-20181121-](https://www.latimes.com/espanol/vidayestilo/la-es-como-evitar-ser-estafado-en-medio-de-todas-esas-buenas-ofertas-del-viernes-negro-20181121-story.html)

[story.html](https://www.latimes.com/espanol/vidayestilo/la-es-como-evitar-ser-estafado-en-medio-de-todas-esas-buenas-ofertas-del-viernes-negro-20181121-story.html)

[https://www.latimes.com/science/story/2019-10-](https://www.latimes.com/science/story/2019-10-21/virus-afm-illness-paralyzing-kids)

[21/virus-afm-illness-paralyzing-kids](https://www.latimes.com/science/story/2019-10-21/virus-afm-illness-paralyzing-kids)

[https://www.latimes.com/espanol/internacional/art](https://www.latimes.com/espanol/internacional/articulo/2019-11-07/desinformacion-y-falta-de-cultura-limitan-el-uso-de-condon-en-latinoamerica)

[iculo/2019-11-07/desinformacion-y-falta-de-cultura-](https://www.latimes.com/espanol/internacional/articulo/2019-11-07/desinformacion-y-falta-de-cultura-limitan-el-uso-de-condon-en-latinoamerica)

[limitan-el-uso-de-condon-en-latinoamerica](https://www.latimes.com/espanol/internacional/articulo/2019-11-07/desinformacion-y-falta-de-cultura-limitan-el-uso-de-condon-en-latinoamerica)

[https://www.latimes.com/espanol/internacional/art](https://www.latimes.com/espanol/internacional/articulo/2019-11-05/ya-esta-a-la-venta-el-renovado-honda-cr-v-del-2020)

[iculo/2019-11-05/ya-esta-a-la-venta-el-renovado-](https://www.latimes.com/espanol/internacional/articulo/2019-11-05/ya-esta-a-la-venta-el-renovado-honda-cr-v-del-2020)

[honda-cr-v-del-2020](https://www.latimes.com/espanol/internacional/articulo/2019-11-05/ya-esta-a-la-venta-el-renovado-honda-cr-v-del-2020)

[https://www.latimes.com/science/sciencenow/la-](https://www.latimes.com/science/sciencenow/la-sci-science-of-zika-five-ways-20160223-htmlstory.html)

[sci-science-of-zika-five-ways-20160223-](https://www.latimes.com/science/sciencenow/la-sci-science-of-zika-five-ways-20160223-htmlstory.html)

[htmlstory.html](https://www.latimes.com/science/sciencenow/la-sci-science-of-zika-five-ways-20160223-htmlstory.html)

[https://www.latimes.com/espanol/internacional/art](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[ ](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)[https://www.latimes.com/espanol/mexico/articulo/](https://www.latimes.com/espanol/mexico/articulo/2019-11-07/nuevos-tratamientos-alargan-hasta-7-anos-vida-de-enfermos-de-cancer-de-pulmon)

[iculo/2019-11-09/monos-se-volvieron-inmunes-tras-](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[inocularles-virus-del-ebola-con-mutacion](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[https://www.latimes.com/science/story/2019-10-](https://www.latimes.com/science/story/2019-10-21/virus-afm-illness-paralyzing-kids)

[21/virus-afm-illness-paralyzing-kids](https://www.latimes.com/science/story/2019-10-21/virus-afm-illness-paralyzing-kids)

[2019-11-07/nuevos-tratamientos-alargan-hasta-7-](https://www.latimes.com/espanol/mexico/articulo/2019-11-07/nuevos-tratamientos-alargan-hasta-7-anos-vida-de-enfermos-de-cancer-de-pulmon)

[anos-vida-de-enfermos-de-cancer-de-pulmon](https://www.latimes.com/espanol/mexico/articulo/2019-11-07/nuevos-tratamientos-alargan-hasta-7-anos-vida-de-enfermos-de-cancer-de-pulmon)

[https://www.latimes.com/espanol/internacional/art](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[iculo/2019-11-09/monos-se-volvieron-inmunes-tras-](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[inocularles-virus-del-ebola-con-mutacion](https://www.latimes.com/espanol/internacional/articulo/2019-11-09/monos-se-volvieron-inmunes-tras-inocularles-virus-del-ebola-con-mutacion)

[https://www.latimes.com/socal/daily-pilot/news/tn-](https://www.latimes.com/socal/daily-pilot/news/tn-dpt-me-west-nile-20170707-story.html)[ ](https://www.latimes.com/socal/daily-pilot/news/tn-dpt-me-west-nile-20170707-story.html)[https://www.latimes.com/california/story/2019-10-](https://www.latimes.com/california/story/2019-10-10/skelton-california-wrong-direction-poll)

[dpt-me-west-nile-20170707-story.html](https://www.latimes.com/socal/daily-pilot/news/tn-dpt-me-west-nile-20170707-story.html)

[https://www.latimes.com/science/sciencenow/la-](https://www.latimes.com/science/sciencenow/la-sci-sn-acute-flaccid-myelitis-polio-20181017-story.html)

[sci-sn-acute-flaccid-myelitis-polio-20181017-](https://www.latimes.com/science/sciencenow/la-sci-sn-acute-flaccid-myelitis-polio-20181017-story.html)

[story.html](https://www.latimes.com/science/sciencenow/la-sci-sn-acute-flaccid-myelitis-polio-20181017-story.html)

[10/skelton-california-wrong-direction-poll](https://www.latimes.com/california/story/2019-10-10/skelton-california-wrong-direction-poll)

[https://www.latimes.com/business/hiltzik/la-fi-](https://www.latimes.com/business/hiltzik/la-fi-hiltzik-drug-prices-20160601-snap-story.html)

[hiltzik-drug-prices-20160601-snap-story.html](https://www.latimes.com/business/hiltzik/la-fi-hiltzik-drug-prices-20160601-snap-story.html)

[https://www.latimes.com/local/lanow/la-me-ln-](https://www.latimes.com/local/lanow/la-me-ln-virulent-newcastle-disease-outbreak-in-southern-california-20190607-story.html)

[virulent-newcastle-disease-outbreak-in-southern-](https://www.latimes.com/local/lanow/la-me-ln-virulent-newcastle-disease-outbreak-in-southern-california-20190607-story.html)

[california-20190607-story.html](https://www.latimes.com/local/lanow/la-me-ln-virulent-newcastle-disease-outbreak-in-southern-california-20190607-story.html)

[https://www.latimes.com/visuals/graphics/la-fg-g-](https://www.latimes.com/visuals/graphics/la-fg-g-zika-abortion-20160225-htmlstory.html)

[zika-abortion-20160225-htmlstory.html](https://www.latimes.com/visuals/graphics/la-fg-g-zika-abortion-20160225-htmlstory.html)

[https://www.latimes.com/local/obituaries/la-me-](https://www.latimes.com/local/obituaries/la-me-deborah-asnis-20150921-story.html)

[deborah-asnis-20150921-story.html](https://www.latimes.com/local/obituaries/la-me-deborah-asnis-20150921-story.html)

[https://www.latimes.com/espanol/vidayestilo/la-es-](https://www.latimes.com/espanol/vidayestilo/la-es-un-nuevo-estudio-de-la-epa-cuantifica-los-efectos-del-cambio-climatico-20190408-story.html)

[un-nuevo-estudio-de-la-epa-cuantifica-los-efectos-](https://www.latimes.com/espanol/vidayestilo/la-es-un-nuevo-estudio-de-la-epa-cuantifica-los-efectos-del-cambio-climatico-20190408-story.html)

[del-cambio-climatico-20190408-story.html](https://www.latimes.com/espanol/vidayestilo/la-es-un-nuevo-estudio-de-la-epa-cuantifica-los-efectos-del-cambio-climatico-20190408-story.html)

[https://www.latimes.com/food/story/2019-10-](https://www.latimes.com/food/story/2019-10-25/plenty-vertical-farm-compton)

[25/plenty-vertical-farm-compton](https://www.latimes.com/food/story/2019-10-25/plenty-vertical-farm-compton)

<https://www.latimes.com/espanol/etiqueta/salud>





Overlap

We can see much difference between the two ranking algorithms as there is no significant overlap between the two. We can

only observe a single overlap on a single query. The overlap graph looks as follows.

Fig. 8. Overlap Graph

