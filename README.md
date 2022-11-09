# java-filmorate
Template repository for Filmorate project.

ERD: 
![ERD](https://github.com/Lex4509/java-filmorate/blob/main/erd%20flmorate.png)


## SQL examples:

SELECT * FROM user; - get all users

SELECT * FROM user WHERE id = n; - get user by id n

SELECT COUNT(id) FROM like WHERE film_id = n; - get film id n likes quantity 

SELECT fs.name  
FROM friendship AS f  
JOIN friendship_status AS fs ON f.friendship_status_id = fs.id  
WHERE friendship.user_id = n  
  AND friensship.friend_id = m; - get users n and m frendship status name
  
SELECT film_id,  
    COUNT(id) AS like quantity  
FROM like  
GROUP BY film_id   
ORDER BY like_quantity DESC   
limit 10; - get 10 most popular film ids and likes quantity
