import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# === Load experimental data ===
# Your CSV file should have columns: n,runtime_ms,defect_regions
data = pd.read_csv("fabric_defect_detection.csv")

# === Extract values ===
n = data["n"]
runtime = data["runtime_ms"]
defects = data["defect_regions"]

# === Compute theoretical trend (O(N log N)) for comparison ===
# Normalize to first runtime to visually align scales
theoretical = (n * np.log2(n))
theoretical = theoretical / theoretical.iloc[0] * runtime.iloc[0]

# === Plot 1: Runtime Scaling ===
plt.figure(figsize=(8, 5))
plt.plot(n, runtime, marker='o', linewidth=2, label='Measured Runtime')
plt.plot(n, theoretical, linestyle='--', color='orange', label='O(N log N) reference')

plt.title("Runtime Analysis of Divide-and-Conquer Fabric Defect Detection", fontsize=11)
plt.xlabel("Image Dimension (n × n)", fontsize=10)
plt.ylabel("Execution Time (ms)", fontsize=10)
plt.grid(True, linestyle='--', alpha=0.6)
plt.legend()
plt.tight_layout()
plt.savefig("runtime_scaling_defect_detection.png", dpi=300)
plt.show()

# === Plot 2: Defects Detected vs Image Size ===
plt.figure(figsize=(8, 5))
plt.plot(n, defects, marker='s', color='green', linewidth=2)
plt.title("Detected Defects vs Image Resolution", fontsize=11)
plt.xlabel("Image Dimension (n × n)", fontsize=10)
plt.ylabel("Number of Defective Regions", fontsize=10)
plt.grid(True, linestyle='--', alpha=0.6)
plt.tight_layout()
plt.savefig("defects_vs_resolution.png", dpi=300)
plt.show()

print("✅ Plots saved as 'runtime_scaling_defect_detection.png' and 'defects_vs_resolution.png'")
