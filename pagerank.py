import networkx as nx


graph = nx.read_edgelist("/Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/edges.txt")
page_rank = nx.pagerank(graph,alpha=0.85, personalization=None, max_iter=30, tol=1e-06, nstart=None, weight='weight',dangling=None)
with open("external_pageRankFile.txt", "w") as f:
    for key, value in page_rank.items():
        f.write(f"/Users/kamalnimmagadda/Desktop/Information Retrieval/HW4/LATIMES/{key}={value}\n")
