# Textile Optimization Algorithms   

## Overview
This project demonstrates how **Greedy** and **Divide & Conquer** algorithms can be applied to real-world textile industry problems — specifically, optimizing fabric cutting schedules and detecting fabric defects from high-resolution images.

It includes:
- **Greedy Fabric Cutting Optimization** (fractional knapsack formulation)
- **Divide & Conquer Fabric Defect Detection** (quadtree image segmentation)
- Experimental validation using **Java** for computation and **Python** for plotting.

## How To Run:

## 1️⃣ Greedy Fabric Cutting Optimization
# Compile:
javac GreedyFabricCutting.java
# Run:
java GreedyFabricCutting
# Output
Displays optimal cutting schedule and total profit.
Saves runtime data in greedy_fabric_cut_timings.csv.
##Visualize results:
python plots.py --mode greedy

## 2️⃣ Divide & Conquer Fabric Defect Detection
# Compile:
javac DivideAndConquerFabricDefectDetection.java ImageCreation.java
# Run:
java ImageCreation
java DivideAndConquerFabricDefectDetection
# Output:
Generates a synthetic fabric image (fabric_with_defects.png).
Detects defects using recursive quadtree decomposition.
Saves data in fabric_defect_detection.csv.
# Visualize results:
python plots.py --mode defect

## Data Visualization
The plots.py script reproduces all plots used in the report:
| Plot File                   | Description                                              |
| --------------------------- | -------------------------------------------------------- |
| `greedy_runtime_plot.png`   | Runtime analysis of the greedy algorithm ($O(n \log n)$) |
| `greedy_value_plot.png`     | Total profit vs number of patterns                       |
| `defects_vs_resolution.png` | Number of detected defects vs image resolution           |
| `fabric_with_defects.png`   | Sample image showing detected defective regions          |

To generate all plots:
python plots.py --mode all

## Algorithm Summary
| Problem          | Technique                    | Time Complexity| 
| ---------------- | ---------------------------- | ---------------------------------------- |
| Fabric Cutting   | Greedy (Fractional Knapsack) | **O(n log n)**                           |
| Defect Detection | Divide & Conquer (Quadtree)  | **Average:** O(N), **Worst:** O(N log N) |

## File Reference
| File                                         | Purpose                                           |
| -------------------------------------------- | ------------------------------------------------- |
| `GreedyFabricCutting.java`                   | Implements greedy optimization for fabric cutting |
| `DivideAndConquerFabricDefectDetection.java` | Quadtree-based fabric defect detection            |
| `ImageCreation.java`                         | Generates synthetic test fabric images            |
| `greedy_fabric_cut_timings.csv`              | Runtime data for greedy algorithm                 |
| `fabric_defect_detection.csv`                | Runtime and detection statistics                  |
| `plots.py`                                   | Python plotting script                            |
| `.png` files                                 | Generated figures for report                      |
| `README.md`                                  | Documentation and usage guide                     |

## Example Usage
To reproduce both experiments
# Compile all Java programs: javac *.java
# Run fabric cutting optimization: java GreedyFabricCutting
# Run image generation and defect detection: java ImageCreation, java DivideAndConquerFabricDefectDetection
# Plot experimental graphs: python plots.py --mode all
