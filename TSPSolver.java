import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TSPSolver {
    // Inner class representing a City with its ID, coordinates, and visited status
    static class City {
        int cityID;
        double cityXLocation;
        double cityYLocation;
        boolean visited;

        // Constructor to initialize a City with ID and coordinates
        public City(int id, double xCoord, double yCoord) {
            cityID = id;
            cityXLocation = xCoord;
            cityYLocation = yCoord;
            visited = false;
        }

        // Getters for X and Y coordinates
        public double getX() {
            return cityXLocation;
        }

        public double getY() {
            return cityYLocation;
        }

        // Method to calculate distance between two cities
        public double distanceCalculator(double x, double y) {
            double distance = Math.sqrt(Math.pow(cityXLocation - x, 2) + Math.pow(cityYLocation - y, 2));
            double distanceRounded = Math.round(distance * 100.0) / 100.0;
            return distanceRounded;
        }

        // Methods to check and set the 'visited' status
        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean value) {
            visited = value;
        }
    }

    // Main method to read city data from a file and solve the TSP
    public static void main(String[] args) {
        //Initializing an empty arraylist called cities which stores city objects from the given file
        List<City> cities = new ArrayList<>();

        //String fileName = "train2.txt"; // File containing city data
        System.out.println("\n\nHello! Welcome to TSPSolver, a program which specializes in solving different Travelling Salesman Problem (TSP) scenarios");
        Scanner fileScanner = new Scanner(System.in);  // Create a Scanner object
        System.out.println("\nEnter name of the file to be scanned (do include .txt extension, and note that this program is case sensitive):");
        String fileName = fileScanner.nextLine();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Read each line of the file containing city data
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+"); // Split each line by multiple spaces
                int cityID = Integer.parseInt(parts[0]);
                double xCoord = Double.parseDouble(parts[1]);
                double yCoord = Double.parseDouble(parts[2]);
                cities.add(new City(cityID, xCoord, yCoord)); // Create City objects and add to the list
            }
            City[] cityArray = cities.toArray(new City[0]); // Convert the list of cities to an array
            cityPrinter(cityArray);
            pathFinder(cityArray);
            //solvingTSP(cityArray); // Solve the Traveling Salesman Problem with the array of cities
        } catch (IOException | NumberFormatException e) {
            // Handle file reading or formatting errors
            System.out.println("\nThere seems to be an error, which may be either of the following: ");
            System.out.println("\t- The file \"" + fileName + "\" does not exist");
            System.out.println("\t- The file is not formatted appropriately");
        }
    }

    public static void cityPrinter(City[] cities){
        System.out.println("\n\nThe following are the coordinates of the cities given\n");
        for (int i = 0; i < cities.length; i++) {
            System.out.println(" -City " + cities[i].cityID + " (" + (int)cities[i].cityXLocation + ", " + (int)cities[i].cityYLocation + ")");
        }
    }

    public static void pathFinder(City[] cities){
        int numOfCities = cities.length;
        double shortestDistance = Double.MAX_VALUE;
        City[] shortestPath = new City[numOfCities + 1]; // Array to store the shortest path

        // Loop through each city as a potential starting point
        for (int i = 0; i < numOfCities; i++) {
            City[] pathSol = new City[numOfCities + 1]; // Store the path for this iteration
            int startCity = i;
            cities[startCity].setVisited(true);
            int pathIndex = 0;
            pathSol[pathIndex++] = cities[startCity]; // Add the start city to the path

            // Greedy algorithm to find the nearest neighbor for each city in the path
            while (pathIndex < numOfCities) {
                int nearestNeighbor = -1; //Initialized as -1, and if it remains the same, then a neighbor is not found
                double minDistance = Double.MAX_VALUE;
                City currentCity = pathSol[pathIndex - 1];

                // Find the nearest unvisited neighbor
                for (int j = 0; j < numOfCities; j++) {
                    if (!cities[j].isVisited()) {
                        double distance = currentCity.distanceCalculator(cities[j].getX(), cities[j].getY());
                        if (distance < minDistance) {
                            minDistance = distance;
                            nearestNeighbor = j;
                        }
                    }
                }

                //If nearestNeighbor is not -1 (initial value), it means that a neighbor is found
                if (nearestNeighbor != -1) {
                    cities[nearestNeighbor].setVisited(true); // A nearest unvisited neighbor is found, so now it will be visited
                    pathSol[pathIndex++] = cities[nearestNeighbor];
                }
            }

            // Complete the path by returning to the start city
            pathSol[pathIndex] = cities[startCity];

            // Calculate total distance for this path
            double totalDistance = 0.0;
            for (int k = 0; k < pathSol.length - 1; k++) {
                totalDistance += pathSol[k].distanceCalculator(pathSol[k + 1].getX(), pathSol[k + 1].getY());
            }

            // Update shortest path and distance if this path is shorter than the current shortest
            if (totalDistance < shortestDistance) {
                shortestDistance = totalDistance;
                shortestPath = pathSol.clone();
            }

            // Reset visited status for the next iteration
            for (City city : cities) {
                city.setVisited(false);
            }
        }

        // Print the shortest path found
        System.out.println("\n\nFinding the most efficient travelling pathway possible:\n");
        for (int i = 0; i < shortestPath.length; i++) {
            System.out.print("| City " + shortestPath[i].cityID + " |");
            if (i < shortestPath.length - 1) {
                System.out.print("  ->  ");
            }
            // Print a newline for better readability after every 5 cities
            if ((i % 5 == 0) && (i != 0)) {
                System.out.print("\n");
            }
        }

        // Print total distance for the shortest path
        double totalDistRounded = Math.round(shortestDistance * 100.0) / 100.0;
        System.out.println("\n\nTotal Distance Covered: " + totalDistRounded + " units");
    }

}





















// Method to solve the Traveling Salesman Problem given an array of cities
//    public static void solvingTSP(City[] cities) {
//        int numOfCities = cities.length;
//        double shortestDistance = Double.MAX_VALUE;
//        City[] shortestPath = new City[numOfCities + 1]; // Array to store the shortest path
//
//        // Loop through each city as a potential starting point
//        for (int i = 0; i < numOfCities; i++) {
//            City[] pathSol = new City[numOfCities + 1]; // Store the path for this iteration
//            int startCity = i;
//            cities[startCity].setVisited(true);
//            int pathIndex = 0;
//            pathSol[pathIndex++] = cities[startCity]; // Add the start city to the path
//
//            // Greedy algorithm to find the nearest neighbor for each city in the path
//            while (pathIndex < numOfCities) {
//                int nearestNeighbor = -1;
//                double minDistance = Double.MAX_VALUE;
//                City currentCity = pathSol[pathIndex - 1];
//
//                // Find the nearest unvisited neighbor
//                for (int j = 0; j < numOfCities; j++) {
//                    if (!cities[j].isVisited()) {
//                        double distance = currentCity.distanceCalculator(cities[j].getX(), cities[j].getY());
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            nearestNeighbor = j;
//                        }
//                    }
//                }
//
//                // Mark the nearest neighbor as visited and add it to the path
//                if (nearestNeighbor != -1) {
//                    cities[nearestNeighbor].setVisited(true);
//                    pathSol[pathIndex++] = cities[nearestNeighbor];
//                }
//            }
//
//            // Complete the path by returning to the start city
//            pathSol[pathIndex] = cities[startCity];
//
//            // Calculate total distance for this path
//            double totalDistance = 0.0;
//            for (int k = 0; k < pathSol.length - 1; k++) {
//                totalDistance += pathSol[k].distanceCalculator(pathSol[k + 1].getX(), pathSol[k + 1].getY());
//            }
//
//            // Update shortest path and distance if this path is shorter than the current shortest
//            if (totalDistance < shortestDistance) {
//                shortestDistance = totalDistance;
//                shortestPath = pathSol.clone();
//            }
//
//            // Reset visited status for the next iteration
//            for (City city : cities) {
//                city.setVisited(false);
//            }
//        }
//
//        System.out.println("\n\nThe following are the coordinates of the cities given\n");
//        for (int i = 0; i < cities.length; i++) {
//            System.out.println(" -City " + cities[i].cityID + " (" + (int)cities[i].cityXLocation + ", " + (int)cities[i].cityYLocation + ")");
//        }
//
//        // Print the shortest path found
//        System.out.println("\n\nFinding the most efficient travelling pathway possible:\n");
//        for (int i = 0; i < shortestPath.length; i++) {
//            System.out.print("| City " + shortestPath[i].cityID + " |");
//            if (i < shortestPath.length - 1) {
//                System.out.print("  ->  ");
//            }
//            // Print a newline for better readability after every 5 cities
//            if ((i % 5 == 0) && (i != 0)) {
//                System.out.print("\n");
//            }
//        }
//
//        // Print total distance for the shortest path
//        double totalDistRounded = Math.round(shortestDistance * 100.0) / 100.0;
//        System.out.println("\n\nTotal Distance Covered: " + totalDistRounded + " units");
//    }
