import numpy as np
import matplotlib.pyplot as plt

# Define the matrix A
A = np.array([[np.sqrt(2)/2, np.sqrt(2)/2],
              [np.sqrt(2)/2, np.sqrt(2)/2]])

# Define eigenvectors
v1 = np.array([1, -1])  # Eigenvector corresponding to eigenvalue 0
v2 = np.array([1, 1])   # Eigenvector corresponding to eigenvalue sqrt(2)
w = np.random.rand(2)   # Random non-eigenvector

# Apply transformation A to eigenvectors and random non-eigenvector
Av1 = np.dot(A, v1)
Av2 = np.dot(A, v2)
Aw = np.dot(A, w)

# Plot original vectors and their images under A
plt.figure(figsize=(8, 8))
plt.quiver(0, 0, v1[0], v1[1], angles='xy', scale_units='xy', scale=1, color='b', label='Eigenvector v1')
plt.quiver(0, 0, v2[0], v2[1], angles='xy', scale_units='xy', scale=1, color='g', label='Eigenvector v2')
plt.quiver(0, 0, w[0], w[1], angles='xy', scale_units='xy', scale=1, color='m', label='Random non-eigenvector')
plt.quiver(0, 0, Av1[0], Av1[1], angles='xy', scale_units='xy', scale=1, color='r', label='Av1')
plt.quiver(0, 0, Av2[0], Av2[1], angles='xy', scale_units='xy', scale=1, color='y', label='Av2')
plt.quiver(0, 0, Aw[0], Aw[1], angles='xy', scale_units='xy', scale=1, color='c', label='Aw')
plt.xlim(-2, 2)
plt.ylim(-2, 2)
plt.xlabel('x')
plt.ylabel('y')
plt.axhline(0, color='k', linestyle='--', linewidth=0.5)
plt.axvline(0, color='k', linestyle='--', linewidth=0.5)
plt.grid(True, linestyle='--', alpha=0.7)
plt.legend()
plt.title('Eigenvectors and their Images under A')
plt.show()
