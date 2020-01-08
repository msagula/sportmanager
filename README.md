# sportmanager

## Description
A soccer game where user buys, sells, and manages teams. The point of this game is to own as many teams as possible, increase Manager's worth and win the league

## Functionality

### Buying a team 
   - A user can click on one of the leagues and buy a team in that league. The team’s cost must be lesser than the Manager’s money. After buying a team, Manager’s money are deducted by whatever that team is worth. All the teams that the use bought can then be viewed in Manager Activity

### Investing 
   - After buying a team, a user can increase team’s worth. This can be done by pressing ‘invest’ button on one of the teams in the Manager activity. This action will increase team’s money by 100 and decrease Manager’s money by 100

### Selling a team
   - In the Manager activity, aside form investing in a team, a user can also sell it. This action will remove the team from Manager activity and add team’s money to Manager money

### Matchweek
  - Each league uses a round robin tournament style. Each team in the league plays against other teams in the same league exactly one time. That means that there is *# of teams–1* matchweeks. Code for round robin tournament: 
  ```java
  ArrayList<Team> finalList = new ArrayList<>();
  ArrayList<Team> tempList = new ArrayList<>(teamList);

  //Team in the 0th position is added to new Array. It will always stay in the 0th position
  Team first = tempList.get(0);
  finalList.add(first);
  tempList.remove(0);

  //Remaining teams of the old ArrayList get rotated to next position by 'week' amount of times
  Collections.rotate(tempList, week);

  //The first half of the old ArrayList gets added to a List
  //Second half gets added to another list
  List<Team> firstHalf = tempList.subList(0, tempList.size()/2);
  List<Team> secondHalf = tempList.subList(tempList.size()/2, tempList.size());

  //Reflects second list
  Collections.reverse(secondHalf);

  //First and seconds list are added to the new ArrayList
  finalList.addAll(firstHalf);
  finalList.addAll(secondHalf);
  ```
  
  
  Then, teams in the first half of the finalList are matched with teams that are in the second half
  
  
  ```java
  int gamesNumber = matchdayTeams.size()/2;
            for(int i = 0; i < gamesNumber; i++) {
                Team t1 = matchdayTeams.get(i);
                Team t2 = matchdayTeams.get(i+gamesNumber);
                ... }
  ```
  
 ### Selecting a game winner
   - Winner: The probability of winning a game is based on two things: team’s points in the league and their worth. Two ints, i1 and i2, are created that hold the total number of team’s points plus money/100. Then 1s and 2s are added *i1* and *i2* amount of times to an Array. The array is shuffled so that the two integers are not grouped together. Then, using a Random(), one of the ints is chosen from the Array. If it's 1, Team1 wins. If it's 2, Team2 wins
   
### Promotion/relegation
   - There are two types of leagues: Promotion and Relegation(PandR) and Franchise. If a league is an instance of PandR and has a league above it, at the end of a season top three teams will be moved to that league. Also, if a league is a PandR and has a league below it, at the end of the season bottom three teams will be moved to that league. In both PandR and Franchise leagues, at the end of season winner of that league gets +$300 
   
### League table
   - Each league has a team table. It is sorted by points descending. Each entry in the table contains team’s name, worth, and points. It also contains a button that allows user to buy selected team
   
### SQLite database
   - Every time an activity is created, it retrieves all the information that it uses in that activity from the SQLite database. After the activity stops, it updates the information that might have changed while using that activity to the SQLite database
   
## Future releases
   - Add players to each team. Each player will have a rating, worth, etc.
   - Add rating for each team. It will be based on the average rating of every players in the club. This rating will be used to determine the winner of the match
   - Improve graphic design of the app
   - Handle different number of teams in league.
   - Add more leagues and teams
   - Optimize code
