#database 
import MySQLdb

import networkx as nx
from random import random
try:
    import matplotlib.pyplot as plt
except:
    raise

db = MySQLdb.connect("localhost","root","pratik","facebook_database" )


## dictionary of id and its users
dict_id_to_user = {}
cursor = db.cursor()
cursor.execute("select * from User");
results_user_name = cursor.fetchall()
for eachid_user in results_user_name : 
    dict_id_to_user[eachid_user[0]] = eachid_user[1] 

##nodes 
cursor = db.cursor()
cursor.execute("select * from ClusterandUser");
results = cursor.fetchall()
#print results

#close database
db.close()

dictcommu = {}
for one_man in results:
    if dictcommu.has_key(one_man[0]) :
        dictcommu[one_man[0]].append(dict_id_to_user[one_man[1]]) 
    else :
        dictcommu[one_man[0]] = []
        dictcommu[one_man[0]].append(dict_id_to_user[one_man[1]])

## community = [[one community],[othercommunity], ..... ]
##all_social_network_graph_user = [[1,2,5],[3,4,6,7]]
all_social_network_graph_user = dictcommu.values()

no_of_communities = len(all_social_network_graph_user)

#if lenght ==3 then add same first ele again   ##bug in Networkx removed
for h in range(no_of_communities):
    if len(all_social_network_graph_user[h])==3:
        all_social_network_graph_user[h].append(all_social_network_graph_user[h][0])

#create graph object
G=nx.Graph()

#add all graph users 
for one_community_no in range(len(all_social_network_graph_user)):
    G.add_nodes_from(all_social_network_graph_user[one_community_no],community_no = one_community_no)

#print G.nodes(data=True)

pos=nx.random_layout(G)
#pos=nx.spring_layout(G,scale=10)

plt.figure(figsize=(8,8))

#draw nodes
for i in range(no_of_communities):
    current_nodes = all_social_network_graph_user[i]
    nx.draw_networkx_nodes(G,pos,node_size=500,nodelist=current_nodes,node_color=(random(), random(),random()))
    ## for all one color
    #nx.draw_networkx_nodes(G,pos,node_size=500,nodelist=current_nodes,node_color='r')


# labels
nx.draw_networkx_labels(G,pos,font_size=10,font_family='sans-serif')

#nx.draw(G)
plt.axis('off')
plt.savefig('ques.jpg')
plt.show()

print all_social_network_graph_user
for one_cluster in all_social_network_graph_user : 
    print len(one_cluster)
