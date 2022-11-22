# java-filmorate
Template repository for Filmorate project.

## ERD: 
![ERD](https://user-images.githubusercontent.com/106984793/203444197-c76b183f-e85b-4ff2-8fff-5b9002f15f1d.png)



## SQL examples:
### get all users:

SELECT * FROM user;
### get user by id n:

SELECT * FROM user WHERE id = n; 
### get film id n likes quantity:

SELECT COUNT(id) FROM like WHERE film_id = n; 
### get users n and m frendship status name:

SELECT fs.name  
FROM friendship AS f  
JOIN friendship_status AS fs ON f.friendship_status_id = fs.id  
WHERE friendship.user_id = n  
  AND friensship.friend_id = m;
### get 10 most popular film ids and likes quantity:
  
SELECT film_id,  
    COUNT(id) AS like_quantity  
FROM like  
GROUP BY film_id   
ORDER BY like_quantity DESC   
limit 10;
