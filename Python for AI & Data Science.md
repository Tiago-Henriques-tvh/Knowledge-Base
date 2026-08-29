# Python for AI & Data Science

---

## Table of Contents

1. [The Data Science Stack Overview](#1-the-data-science-stack-overview)
2. [NumPy — Numerical Computing](#2-numpy--numerical-computing)
3. [Pandas — Data Manipulation](#3-pandas--data-manipulation)
4. [Data Visualization (Matplotlib & Seaborn)](#4-data-visualization-matplotlib--seaborn)
5. [Scikit-learn — Machine Learning](#5-scikit-learn--machine-learning)
6. [Working with Real Datasets](#6-working-with-real-datasets)
7. [Serving ML Models via Flask](#7-serving-ml-models-via-flask)
8. [Environment & Dependency Management](#8-environment--dependency-management)
9. [Testing Data & ML Code](#9-testing-data--ml-code)
10. [Performance Tips](#10-performance-tips)
11. [Complete Cheat Sheet](#11-complete-cheat-sheet)

---

## 1. The Data Science Stack Overview

### 1.1 How the Libraries Fit Together

```
┌─────────────────────────────────────────────────────────┐
│                  THE PYTHON AI/DS STACK                 │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  Raw Data (CSV, JSON, SQL, APIs)                        │
│         ↓                                               │
│  Pandas          ← Load, clean, transform, explore      │
│         ↓                                               │
│  NumPy           ← Fast numerical operations (Pandas    │
│                     is built ON TOP of NumPy)           │
│         ↓                                               │
│  Matplotlib/     ← Visualize distributions, trends      │
│  Seaborn                                                │
│         ↓                                               │
│  Scikit-learn    ← Train models, evaluate, predict      │
│         ↓                                               │
│  Flask           ← Serve predictions via API            │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 1.2 Installing the Stack

```bash
pip install numpy pandas matplotlib seaborn scikit-learn flask --break-system-packages

# Or with a requirements.txt
cat > requirements.txt << 'EOF'
numpy>=1.24.0
pandas>=2.0.0
matplotlib>=3.7.0
seaborn>=0.12.0
scikit-learn>=1.3.0
flask>=3.0.0
EOF

pip install -r requirements.txt
```

### 1.3 Standard Import Conventions

Every data science script starts the same way — these aliases are **universal conventions**, always use them:

```python
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.model_selection import train_test_split
```

---

## 2. NumPy — Numerical Computing

### 2.1 Why NumPy Exists

Python's native lists are slow for numerical operations because each element is a full Python object. **NumPy arrays** store data in contiguous memory blocks of a single type, enabling vectorized operations (no Python-level loops needed).

```python
# Native Python (slow for large data)
result = [x * 2 for x in range(1_000_000)]

# NumPy (vectorized, much faster - runs in optimized C code)
import numpy as np
arr = np.arange(1_000_000)
result = arr * 2
```

**Speed comparison mental model:**

```
Python list operations:  ~100x slower
NumPy vectorized ops:    baseline (fast, C-level loops)
```

### 2.2 Creating Arrays

```python
import numpy as np

# From a list
arr = np.array([1, 2, 3, 4, 5])
print(arr)  # [1 2 3 4 5]

# 2D array (matrix)
matrix = np.array([[1, 2, 3], [4, 5, 6]])
print(matrix.shape)  # (2, 3) → 2 rows, 3 columns

# Common creation functions
zeros = np.zeros((3, 4))          # 3x4 matrix of zeros
ones = np.ones((2, 2))            # 2x2 matrix of ones
identity = np.eye(3)              # 3x3 identity matrix
full = np.full((2, 3), 7)         # 2x3 matrix filled with 7

# Ranges
arr1 = np.arange(0, 10, 2)        # [0 2 4 6 8] (start, stop, step)
arr2 = np.linspace(0, 1, 5)       # [0. 0.25 0.5 0.75 1.] (5 evenly spaced)

# Random arrays (crucial for ML — initializing weights, sampling data)
rand_arr = np.random.rand(3, 3)           # Uniform [0, 1)
randn_arr = np.random.randn(3, 3)         # Normal distribution (mean 0, std 1)
randint_arr = np.random.randint(0, 100, size=(3, 3))  # Random integers

# Reproducible randomness (IMPORTANT for ML experiments)
np.random.seed(42)  # Same seed = same "random" results every run
```

### 2.3 Array Attributes

```python
arr = np.array([[1, 2, 3], [4, 5, 6]])

arr.shape       # (2, 3) - dimensions
arr.ndim        # 2 - number of dimensions
arr.size        # 6 - total number of elements
arr.dtype       # dtype('int64') - data type
arr.itemsize    # 8 - bytes per element
```

### 2.4 Indexing & Slicing

```python
arr = np.array([10, 20, 30, 40, 50])

# Basic indexing
arr[0]          # 10
arr[-1]         # 50 (last element)
arr[1:3]        # [20 30] (slice)
arr[::-1]       # [50 40 30 20 10] (reverse)

# 2D indexing
matrix = np.array([[1, 2, 3], [4, 5, 6], [7, 8, 9]])
matrix[0, 1]    # 2 (row 0, column 1)
matrix[:, 0]    # [1 4 7] (all rows, column 0)
matrix[1, :]    # [4 5 6] (row 1, all columns)
matrix[0:2, 1:3]  # [[2 3] [5 6]] (submatrix)

# Boolean masking (extremely common in data science)
arr = np.array([1, -2, 3, -4, 5])
mask = arr > 0
print(mask)        # [True False True False True]
print(arr[mask])   # [1 3 5] (only positive values)

# Shorthand
positive_values = arr[arr > 0]

# Fancy indexing
indices = [0, 2, 4]
arr[indices]    # [1 3 5]
```

### 2.5 Array Operations (Vectorization)

```python
a = np.array([1, 2, 3])
b = np.array([4, 5, 6])

# Element-wise operations (no loops needed!)
a + b           # [5 7 9]
a - b           # [-3 -3 -3]
a * b           # [4 10 18]
a / b           # [0.25 0.4 0.5]
a ** 2          # [1 4 9]

# Scalar broadcasting
a * 2           # [2 4 6]
a + 10          # [11 12 13]

# Comparison operations
a > 1           # [False True True]

# Aggregate functions
arr = np.array([1, 2, 3, 4, 5])
arr.sum()       # 15
arr.mean()      # 3.0
arr.std()       # Standard deviation
arr.var()       # Variance
arr.min()       # 1
arr.max()       # 5
arr.argmax()    # 4 (index of max value)
arr.argmin()    # 0 (index of min value)

# 2D aggregate (with axis)
matrix = np.array([[1, 2, 3], [4, 5, 6]])
matrix.sum()           # 21 (all elements)
matrix.sum(axis=0)     # [5 7 9] (sum each column)
matrix.sum(axis=1)     # [6 15] (sum each row)
```

### 2.6 Reshaping Arrays

```python
arr = np.arange(12)  # [0 1 2 3 4 5 6 7 8 9 10 11]

# Reshape to 2D
matrix = arr.reshape(3, 4)   # 3 rows, 4 columns
matrix = arr.reshape(4, 3)   # 4 rows, 3 columns

# Use -1 to auto-calculate a dimension
matrix = arr.reshape(3, -1)  # 3 rows, auto-calculate columns (4)

# Flatten back to 1D
flat = matrix.flatten()      # Returns a copy
flat = matrix.ravel()        # Returns a view (faster, shares memory)

# Transpose (swap rows/columns)
matrix.T
matrix.transpose()

# Stack arrays
a = np.array([1, 2, 3])
b = np.array([4, 5, 6])
np.vstack([a, b])   # Stack vertically: [[1 2 3] [4 5 6]]
np.hstack([a, b])   # Stack horizontally: [1 2 3 4 5 6]
np.concatenate([a, b])  # Same as hstack for 1D
```

### 2.7 Linear Algebra (Foundation for ML)

```python
A = np.array([[1, 2], [3, 4]])
B = np.array([[5, 6], [7, 8]])

# Matrix multiplication (THE core operation in neural networks)
np.dot(A, B)
A @ B           # Same thing, modern syntax

# Element-wise multiplication (different from matrix mult!)
A * B

# Matrix inverse
np.linalg.inv(A)

# Determinant
np.linalg.det(A)

# Eigenvalues/eigenvectors
eigenvalues, eigenvectors = np.linalg.eig(A)

# Solve linear system Ax = b
b = np.array([1, 2])
x = np.linalg.solve(A, b)
```

### 2.8 Handling Missing/Invalid Data

```python
arr = np.array([1, 2, np.nan, 4, np.nan])

np.isnan(arr)           # [False False True False True]
np.nanmean(arr)         # Mean ignoring NaN values
np.nanmax(arr)          # Max ignoring NaN
arr[~np.isnan(arr)]     # Remove NaN values (~ = NOT)

# Infinity handling
arr2 = np.array([1, np.inf, -np.inf, 3])
np.isinf(arr2)          # [False True True False]
np.isfinite(arr2)       # [True False False True]
```

---

## 3. Pandas — Data Manipulation

### 3.1 The Two Core Data Structures

```
Series (1D)              DataFrame (2D)
┌─────────┐              ┌──────┬──────┬──────┐
│ index │ value │        │ idx  │ colA │ colB │
├─────────┤              ├──────┼──────┼──────┤
│  0    │  10   │        │  0   │  10  │  'x' │
│  1    │  20   │        │  1   │  20  │  'y' │
│  2    │  30   │        │  2   │  30  │  'z' │
└─────────┘              └──────┴──────┴──────┘

A Series is like one column.
A DataFrame is like a spreadsheet/SQL table (a collection of Series sharing an index).
```

### 3.2 Creating Series and DataFrames

```python
import pandas as pd

# Series
s = pd.Series([10, 20, 30, 40])
s = pd.Series([10, 20, 30], index=['a', 'b', 'c'])

# DataFrame from a dictionary
df = pd.DataFrame({
    'name': ['Alice', 'Bob', 'Carol'],
    'age': [25, 30, 35],
    'city': ['NYC', 'LA', 'Chicago']
})

# DataFrame from a list of lists
df = pd.DataFrame(
    [[1, 'Alice', 25], [2, 'Bob', 30]],
    columns=['id', 'name', 'age']
)

# DataFrame from CSV / JSON / Excel / SQL
df = pd.read_csv('data.csv')
df = pd.read_json('data.json')
df = pd.read_excel('data.xlsx', sheet_name='Sheet1')
df = pd.read_sql('SELECT * FROM users', connection)
```

### 3.3 Exploring a DataFrame

```python
df.head()          # First 5 rows
df.head(10)        # First 10 rows
df.tail()          # Last 5 rows
df.shape            # (rows, columns)
df.info()           # Column types, non-null counts, memory usage
df.describe()       # Statistical summary (mean, std, min, max, quartiles)
df.columns           # Column names
df.dtypes            # Data type of each column
df.index              # Row index
df.nunique()         # Number of unique values per column
df['column'].unique()   # Unique values in a specific column
df['column'].value_counts()  # Frequency count of each value
```

### 3.4 Selecting Data

```python
# Select a column (returns a Series)
df['name']
df.name             # Same, dot notation (only works without spaces in name)

# Select multiple columns (returns a DataFrame)
df[['name', 'age']]

# Select rows by position: iloc
df.iloc[0]          # First row
df.iloc[0:3]        # First 3 rows
df.iloc[0, 1]       # Row 0, column 1 (by position)
df.iloc[:, 0]       # All rows, column 0

# Select rows by label: loc
df.loc[0]            # Row with index label 0
df.loc[0:2]          # Rows with index labels 0 through 2 (inclusive!)
df.loc[0, 'name']    # Row 0, column 'name'
df.loc[df['age'] > 25]  # Boolean filtering

# Filtering (the most common operation)
df[df['age'] > 25]
df[(df['age'] > 25) & (df['city'] == 'NYC')]   # AND: use &
df[(df['age'] > 25) | (df['city'] == 'NYC')]   # OR: use |
df[~(df['age'] > 25)]                           # NOT: use ~

# isin() for multiple value matching
df[df['city'].isin(['NYC', 'LA'])]

# String filtering
df[df['name'].str.contains('Al')]
df[df['name'].str.startswith('A')]
df[df['name'].str.lower() == 'alice']
```

### 3.5 Modifying Data

```python
# Add a new column
df['age_in_months'] = df['age'] * 12

# Add column based on condition
df['is_adult'] = df['age'] >= 18

# Apply a function to a column
df['name_upper'] = df['name'].apply(lambda x: x.upper())
df['age_squared'] = df['age'].apply(lambda x: x ** 2)

# Apply function to entire row
df['summary'] = df.apply(lambda row: f"{row['name']} is {row['age']}", axis=1)

# Rename columns
df.rename(columns={'name': 'full_name'}, inplace=True)

# Drop columns/rows
df.drop('age_squared', axis=1, inplace=True)    # Drop column
df.drop(0, axis=0, inplace=True)                # Drop row with index 0

# Sort
df.sort_values('age')                    # Ascending
df.sort_values('age', ascending=False)   # Descending
df.sort_values(['city', 'age'])          # Multiple columns

# Reset index (after filtering/sorting)
df.reset_index(drop=True, inplace=True)
```

### 3.6 Handling Missing Data

```python
df.isnull()             # Boolean mask of missing values
df.isnull().sum()       # Count of missing values per column
df.notnull()            # Opposite of isnull()

# Drop missing values
df.dropna()                      # Drop rows with ANY NaN
df.dropna(subset=['age'])        # Drop rows where 'age' is NaN
df.dropna(axis=1)                # Drop columns with ANY NaN
df.dropna(thresh=2)              # Keep rows with at least 2 non-NaN

# Fill missing values
df.fillna(0)                              # Fill all NaN with 0
df['age'].fillna(df['age'].mean(), inplace=True)  # Fill with mean
df.fillna(method='ffill')                 # Forward fill (use previous value)
df.fillna(method='bfill')                 # Backward fill (use next value)

# Interpolation
df['age'].interpolate()          # Linear interpolation for missing values
```

### 3.7 Grouping & Aggregating (GroupBy)

```python
# Basic groupby
df.groupby('city')['age'].mean()      # Average age per city
df.groupby('city')['age'].sum()       # Total age per city
df.groupby('city').size()             # Count of rows per city
df.groupby('city')['age'].agg(['mean', 'min', 'max', 'count'])

# Multiple grouping columns
df.groupby(['city', 'is_adult'])['age'].mean()

# Custom aggregation
df.groupby('city').agg({
    'age': 'mean',
    'name': 'count'
})

# Groupby with apply (custom function)
df.groupby('city').apply(lambda group: group['age'].max() - group['age'].min())
```

### 3.8 Merging & Joining DataFrames

```python
# Similar to SQL JOINs
df1 = pd.DataFrame({'id': [1, 2, 3], 'name': ['A', 'B', 'C']})
df2 = pd.DataFrame({'id': [1, 2, 4], 'score': [90, 85, 70]})

# Inner join (only matching ids)
pd.merge(df1, df2, on='id', how='inner')

# Left join (keep all from df1)
pd.merge(df1, df2, on='id', how='left')

# Right join (keep all from df2)
pd.merge(df1, df2, on='id', how='right')

# Outer join (keep all from both)
pd.merge(df1, df2, on='id', how='outer')

# Concatenate (stack DataFrames)
pd.concat([df1, df2])              # Stack rows (vertically)
pd.concat([df1, df2], axis=1)      # Stack columns (horizontally)

# Join on index
df1.join(df2, how='left')
```

### 3.9 Pivot Tables & Reshaping

```python
# Pivot table (like Excel pivot tables)
df.pivot_table(
    values='score',
    index='city',
    columns='is_adult',
    aggfunc='mean'
)

# Melt (wide to long format)
df_wide = pd.DataFrame({
    'id': [1, 2],
    'math': [90, 85],
    'science': [95, 80]
})
df_long = df_wide.melt(id_vars='id', var_name='subject', value_name='score')

# Pivot (long to wide, opposite of melt)
df_long.pivot(index='id', columns='subject', values='score')
```

### 3.10 Time Series (Common in AI applications)

```python
# Parse dates
df['date'] = pd.to_datetime(df['date'])

# Extract date components
df['year'] = df['date'].dt.year
df['month'] = df['date'].dt.month
df['day_of_week'] = df['date'].dt.dayofweek

# Set date as index (enables time-based operations)
df.set_index('date', inplace=True)

# Resample (aggregate by time period)
df.resample('D').mean()    # Daily average
df.resample('M').sum()     # Monthly sum
df.resample('W').max()     # Weekly max

# Rolling window (moving average - common in ML feature engineering)
df['rolling_avg'] = df['value'].rolling(window=7).mean()  # 7-day moving average

# Shift (lag features - very common for time series ML)
df['previous_value'] = df['value'].shift(1)     # Previous row's value
df['value_change'] = df['value'] - df['previous_value']
```

### 3.11 Exporting Data

```python
df.to_csv('output.csv', index=False)
df.to_json('output.json', orient='records')
df.to_excel('output.xlsx', index=False)
df.to_sql('table_name', connection, if_exists='replace')
```

---

## 4. Data Visualization (Matplotlib & Seaborn)

### 4.1 Matplotlib Basics

```python
import matplotlib.pyplot as plt

# Simple line plot
plt.plot([1, 2, 3, 4], [10, 20, 25, 30])
plt.xlabel('X Axis')
plt.ylabel('Y Axis')
plt.title('My Plot')
plt.show()

# Multiple lines
plt.plot(x, y1, label='Series 1')
plt.plot(x, y2, label='Series 2')
plt.legend()
plt.show()

# Common plot types
plt.bar(categories, values)          # Bar chart
plt.scatter(x, y)                    # Scatter plot
plt.hist(data, bins=20)              # Histogram
plt.boxplot(data)                    # Box plot
plt.pie(sizes, labels=labels)        # Pie chart

# Subplots (multiple plots in one figure)
fig, axes = plt.subplots(1, 2, figsize=(10, 4))
axes[0].plot(x, y1)
axes[0].set_title('Plot 1')
axes[1].scatter(x, y2)
axes[1].set_title('Plot 2')
plt.tight_layout()
plt.show()

# Save figure
plt.savefig('output.png', dpi=300, bbox_inches='tight')
```

### 4.2 Seaborn (Statistical Visualization, built on Matplotlib)

```python
import seaborn as sns

# Set style
sns.set_style('whitegrid')

# Distribution plots
sns.histplot(df['age'], bins=20, kde=True)  # Histogram + density curve
sns.boxplot(x='city', y='age', data=df)     # Box plot by category
sns.violinplot(x='city', y='age', data=df)  # Violin plot

# Relationship plots
sns.scatterplot(x='age', y='score', data=df, hue='city')  # Colored by category
sns.lineplot(x='date', y='value', data=df)
sns.regplot(x='age', y='score', data=df)   # Scatter + regression line

# Categorical plots
sns.barplot(x='city', y='age', data=df)
sns.countplot(x='city', data=df)            # Count of each category

# Correlation heatmap (VERY common in ML feature analysis)
correlation_matrix = df.corr(numeric_only=True)
sns.heatmap(correlation_matrix, annot=True, cmap='coolwarm')

# Pairplot (all combinations of numeric columns)
sns.pairplot(df, hue='city')

plt.show()
```

### 4.3 Common Visualization Patterns for ML

```python
# Distribution before/after normalization
fig, axes = plt.subplots(1, 2, figsize=(12, 4))
sns.histplot(df['age'], ax=axes[0])
axes[0].set_title('Before Normalization')
sns.histplot(normalized_age, ax=axes[1])
axes[1].set_title('After Normalization')

# Model performance visualization
plt.plot(history['loss'], label='Training Loss')
plt.plot(history['val_loss'], label='Validation Loss')
plt.xlabel('Epoch')
plt.ylabel('Loss')
plt.legend()
plt.title('Training Progress')

# Confusion matrix
from sklearn.metrics import confusion_matrix
cm = confusion_matrix(y_true, y_pred)
sns.heatmap(cm, annot=True, fmt='d', cmap='Blues')
plt.xlabel('Predicted')
plt.ylabel('Actual')
```

---

## 5. Scikit-learn — Machine Learning

### 5.1 The Standard ML Workflow

```
1. Load Data          pd.read_csv()
2. Clean Data         dropna(), fillna()
3. Feature Engineer   Create/transform columns
4. Split Data         train_test_split()
5. Scale/Normalize    StandardScaler()
6. Train Model        model.fit()
7. Predict            model.predict()
8. Evaluate           accuracy_score(), etc.
9. Tune               GridSearchCV
10. Save Model        joblib.dump()
```

### 5.2 Splitting Data

```python
from sklearn.model_selection import train_test_split

X = df[['age', 'income']]     # Features
y = df['purchased']            # Target/label

X_train, X_test, y_train, y_test = train_test_split(
    X, y,
    test_size=0.2,          # 20% for testing
    random_state=42          # Reproducibility
)

# Stratified split (preserves class proportions - important for imbalanced data)
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)
```

### 5.3 Preprocessing

```python
from sklearn.preprocessing import StandardScaler, MinMaxScaler, LabelEncoder, OneHotEncoder

# Standardization (mean=0, std=1) - most common for numeric features
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)    # Fit on training data
X_test_scaled = scaler.transform(X_test)          # Transform test data (NO re-fit!)

# Min-Max scaling (scale to range [0, 1])
minmax = MinMaxScaler()
X_scaled = minmax.fit_transform(X_train)

# Encoding categorical variables
le = LabelEncoder()
df['city_encoded'] = le.fit_transform(df['city'])  # 'NYC' -> 0, 'LA' -> 1, etc.

# One-hot encoding (avoids implying false ordinal relationships)
df_encoded = pd.get_dummies(df, columns=['city'])
```

> **Critical rule:** Always `fit_transform()` on training data, and only `transform()` (never re-fit) on test data — otherwise you leak information from the test set into your preprocessing, inflating your accuracy score artificially.

### 5.4 Classification Models

```python
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.svm import SVC
from sklearn.neighbors import KNeighborsClassifier

# Every scikit-learn model follows the SAME interface:
model = LogisticRegression()
model.fit(X_train, y_train)               # Train
predictions = model.predict(X_test)        # Predict
probabilities = model.predict_proba(X_test)  # Prediction probabilities

# Random Forest (usually a strong default choice)
rf_model = RandomForestClassifier(n_estimators=100, random_state=42)
rf_model.fit(X_train, y_train)

# Feature importance (which features matter most)
importances = rf_model.feature_importances_
feature_importance_df = pd.DataFrame({
    'feature': X.columns,
    'importance': importances
}).sort_values('importance', ascending=False)
```

### 5.5 Regression Models

```python
from sklearn.linear_model import LinearRegression, Ridge, Lasso
from sklearn.ensemble import RandomForestRegressor

model = LinearRegression()
model.fit(X_train, y_train)
predictions = model.predict(X_test)

# Access coefficients (for linear models)
model.coef_          # Weight for each feature
model.intercept_     # Bias term
```

### 5.6 Evaluation Metrics

```python
from sklearn.metrics import (
    accuracy_score, precision_score, recall_score, f1_score,
    confusion_matrix, classification_report,
    mean_squared_error, mean_absolute_error, r2_score
)

# Classification metrics
accuracy_score(y_test, predictions)
precision_score(y_test, predictions)
recall_score(y_test, predictions)
f1_score(y_test, predictions)
print(classification_report(y_test, predictions))  # All metrics at once

# Confusion matrix
cm = confusion_matrix(y_test, predictions)
#                Predicted Negative  Predicted Positive
# Actual Negative       TN                  FP
# Actual Positive       FN                  TP

# Regression metrics
mean_squared_error(y_test, predictions)
mean_absolute_error(y_test, predictions)
r2_score(y_test, predictions)   # Closer to 1.0 = better fit
```

### 5.7 Cross-Validation (More Reliable Evaluation)

```python
from sklearn.model_selection import cross_val_score, KFold

# 5-fold cross-validation
scores = cross_val_score(model, X, y, cv=5, scoring='accuracy')
print(f"Mean accuracy: {scores.mean():.3f} (+/- {scores.std():.3f})")

# Custom K-Fold
kf = KFold(n_splits=5, shuffle=True, random_state=42)
scores = cross_val_score(model, X, y, cv=kf)
```

### 5.8 Hyperparameter Tuning

```python
from sklearn.model_selection import GridSearchCV, RandomizedSearchCV

# Define parameter grid
param_grid = {
    'n_estimators': [50, 100, 200],
    'max_depth': [None, 10, 20, 30]
}

grid_search = GridSearchCV(
    RandomForestClassifier(random_state=42),
    param_grid,
    cv=5,
    scoring='accuracy'
)
grid_search.fit(X_train, y_train)

print(grid_search.best_params_)     # Best hyperparameters found
print(grid_search.best_score_)      # Best cross-validation score
best_model = grid_search.best_estimator_
```

### 5.9 Pipelines (Combine Preprocessing + Model)

```python
from sklearn.pipeline import Pipeline

# Chain preprocessing and model together (prevents data leakage bugs)
pipeline = Pipeline([
    ('scaler', StandardScaler()),
    ('classifier', RandomForestClassifier(random_state=42))
])

pipeline.fit(X_train, y_train)
predictions = pipeline.predict(X_test)

# Now the pipeline can be saved and reused as ONE object
```

### 5.10 Saving & Loading Models

```python
import joblib

# Save
joblib.dump(model, 'model.pkl')
joblib.dump(pipeline, 'pipeline.pkl')   # Saves preprocessing + model together

# Load
loaded_model = joblib.load('model.pkl')
predictions = loaded_model.predict(new_data)
```

---

## 6. Working with Real Datasets

### 6.1 Complete End-to-End Example

```python
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import classification_report
import joblib

# 1. Load data
df = pd.read_csv('customer_data.csv')

# 2. Explore
print(df.info())
print(df.describe())
print(df.isnull().sum())

# 3. Clean
df['age'].fillna(df['age'].median(), inplace=True)
df.dropna(subset=['target'], inplace=True)  # Can't impute the label

# 4. Feature engineering
df['income_per_age'] = df['income'] / df['age']
df = pd.get_dummies(df, columns=['category'])

# 5. Split features and target
X = df.drop('target', axis=1)
y = df['target']

X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)

# 6. Scale
scaler = StandardScaler()
X_train_scaled = scaler.fit_transform(X_train)
X_test_scaled = scaler.transform(X_test)

# 7. Train
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train_scaled, y_train)

# 8. Evaluate
predictions = model.predict(X_test_scaled)
print(classification_report(y_test, predictions))

# 9. Save everything needed for production
joblib.dump(model, 'model.pkl')
joblib.dump(scaler, 'scaler.pkl')
joblib.dump(list(X.columns), 'feature_names.pkl')  # Remember column order!
```

### 6.2 Common Data Cleaning Patterns

```python
# Remove duplicates
df.drop_duplicates(inplace=True)

# Fix inconsistent text (common in real-world data)
df['city'] = df['city'].str.strip().str.title()  # " nyc " -> "Nyc"

# Detect and handle outliers (IQR method)
Q1 = df['income'].quantile(0.25)
Q3 = df['income'].quantile(0.75)
IQR = Q3 - Q1
lower_bound = Q1 - 1.5 * IQR
upper_bound = Q3 + 1.5 * IQR
df_no_outliers = df[(df['income'] >= lower_bound) & (df['income'] <= upper_bound)]

# Convert data types
df['age'] = df['age'].astype(int)
df['price'] = df['price'].astype(float)
df['date'] = pd.to_datetime(df['date'])

# Validate data
assert df['age'].min() >= 0, "Found negative ages!"
assert df.duplicated().sum() == 0, "Found duplicate rows!"
```

---

## 7. Serving ML Models via Flask

### 7.1 Basic Model Serving API

```python
from flask import Flask, request, jsonify
import joblib
import numpy as np

app = Flask(__name__)

# Load model once at startup (not per-request!)
model = joblib.load('model.pkl')
scaler = joblib.load('scaler.pkl')
feature_names = joblib.load('feature_names.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    if not request.is_json:
        return jsonify({"error": "Content-Type must be application/json"}), 415

    data = request.get_json()

    # Validate required fields
    missing = [f for f in feature_names if f not in data]
    if missing:
        return jsonify({"error": f"Missing fields: {missing}"}), 400

    # Build feature array in the correct order
    features = np.array([[data[f] for f in feature_names]])

    # Preprocess (must match training pipeline exactly!)
    features_scaled = scaler.transform(features)

    # Predict
    prediction = model.predict(features_scaled)[0]
    probability = model.predict_proba(features_scaled)[0].max()

    return jsonify({
        "prediction": int(prediction),
        "confidence": round(float(probability), 4)
    }), 200

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({"status": "healthy", "model_loaded": model is not None}), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
```

### 7.2 Better Practice: Using a Pipeline (Simpler)

```python
from flask import Flask, request, jsonify
import joblib
import pandas as pd

app = Flask(__name__)

# Pipeline includes preprocessing + model as ONE object
pipeline = joblib.load('pipeline.pkl')

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    df = pd.DataFrame([data])   # Convert JSON to DataFrame (same format as training)

    prediction = pipeline.predict(df)[0]

    return jsonify({"prediction": int(prediction)}), 200
```

### 7.3 Batch Predictions

```python
@app.route('/predict/batch', methods=['POST'])
def predict_batch():
    data = request.get_json()
    records = data.get('records', [])

    if not records:
        return jsonify({"error": "No records provided"}), 400

    df = pd.DataFrame(records)
    predictions = pipeline.predict(df)

    results = [
        {"index": i, "prediction": int(pred)}
        for i, pred in enumerate(predictions)
    ]

    return jsonify({"predictions": results}), 200
```

---

## 8. Environment & Dependency Management

### 8.1 Virtual Environments

```bash
# Create a virtual environment
python3 -m venv venv

# Activate it
source venv/bin/activate      # Linux/Mac
venv\Scripts\activate         # Windows

# Install packages inside the venv
pip install numpy pandas scikit-learn flask

# Freeze current dependencies
pip freeze > requirements.txt

# Install from requirements.txt (on another machine)
pip install -r requirements.txt

# Deactivate
deactivate
```

### 8.2 Recommended requirements.txt for AI Projects

```
numpy==1.26.4
pandas==2.2.1
matplotlib==3.8.3
seaborn==0.13.2
scikit-learn==1.4.1
flask==3.0.2
joblib==1.3.2
python-dotenv==1.0.1
gunicorn==21.2.0
```

### 8.3 Environment Variables (.env)

```python
# .env file
MODEL_PATH=models/production_model.pkl
DEBUG=False
API_KEY=your-secret-key

# In your Flask app
from dotenv import load_dotenv
import os

load_dotenv()
MODEL_PATH = os.getenv('MODEL_PATH')
DEBUG = os.getenv('DEBUG', 'False') == 'True'
```

---

## 9. Testing Data & ML Code

### 9.1 Testing Data Processing Functions

```python
import unittest
import pandas as pd
import numpy as np

def clean_age_column(df: pd.DataFrame) -> pd.DataFrame:
    """Fills missing ages with median, removes negative values."""
    df = df.copy()
    df.loc[df['age'] < 0, 'age'] = np.nan
    df['age'].fillna(df['age'].median(), inplace=True)
    return df

class TestDataCleaning(unittest.TestCase):
    def setUp(self):
        self.df = pd.DataFrame({
            'age': [25, -5, np.nan, 40, 30]
        })

    def test_negative_ages_removed(self):
        result = clean_age_column(self.df)
        self.assertTrue((result['age'] >= 0).all())

    def test_no_missing_values(self):
        result = clean_age_column(self.df)
        self.assertEqual(result['age'].isnull().sum(), 0)

if __name__ == '__main__':
    unittest.main()
```

### 9.2 Testing Model Predictions (Sanity Checks)

```python
import unittest
import numpy as np
import joblib

class TestModelSanity(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.model = joblib.load('model.pkl')
        cls.scaler = joblib.load('scaler.pkl')

    def test_prediction_output_shape(self):
        """Model should output one prediction per input row."""
        X = np.random.rand(5, 3)  # 5 samples, 3 features
        X_scaled = self.scaler.transform(X)
        predictions = self.model.predict(X_scaled)
        self.assertEqual(len(predictions), 5)

    def test_prediction_valid_classes(self):
        """Predictions should only be from known classes."""
        X = np.random.rand(10, 3)
        X_scaled = self.scaler.transform(X)
        predictions = self.model.predict(X_scaled)
        self.assertTrue(set(predictions).issubset({0, 1}))

    def test_model_deterministic(self):
        """Same input should always give same output."""
        X = np.array([[1.0, 2.0, 3.0]])
        X_scaled = self.scaler.transform(X)
        pred1 = self.model.predict(X_scaled)
        pred2 = self.model.predict(X_scaled)
        np.testing.assert_array_equal(pred1, pred2)
```

### 9.3 Testing Flask ML Endpoints

```python
import unittest
from app import app

class TestPredictEndpoint(unittest.TestCase):
    def setUp(self):
        self.client = app.test_client()

    def test_valid_prediction_request(self):
        response = self.client.post('/predict', json={
            'age': 30, 'income': 50000, 'score': 0.7
        })
        self.assertEqual(response.status_code, 200)
        self.assertIn('prediction', response.get_json())

    def test_missing_field_returns_400(self):
        response = self.client.post('/predict', json={'age': 30})
        self.assertEqual(response.status_code, 400)

    def test_non_json_request_returns_415(self):
        response = self.client.post('/predict', data="not json")
        self.assertEqual(response.status_code, 415)

    def test_health_check(self):
        response = self.client.get('/health')
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response.get_json()['status'], 'healthy')
```

---

## 10. Performance Tips

### 10.1 NumPy/Pandas Efficiency

```python
# ❌ Slow: Python loop over DataFrame rows
for i, row in df.iterrows():
    df.at[i, 'new_col'] = row['a'] + row['b']

# ✓ Fast: Vectorized operation
df['new_col'] = df['a'] + df['b']

# ❌ Slow: apply() with a Python function for simple math
df['squared'] = df['value'].apply(lambda x: x ** 2)

# ✓ Fast: Direct vectorized operation
df['squared'] = df['value'] ** 2

# ❌ Slow: Growing a DataFrame in a loop
result = pd.DataFrame()
for chunk in chunks:
    result = result.append(chunk)   # Also deprecated in modern pandas!

# ✓ Fast: Collect then concatenate once
chunks_list = [process(chunk) for chunk in chunks]
result = pd.concat(chunks_list, ignore_index=True)
```

### 10.2 Memory Optimization

```python
# Check memory usage
df.info(memory_usage='deep')

# Downcast numeric types to save memory
df['age'] = pd.to_numeric(df['age'], downcast='integer')
df['price'] = pd.to_numeric(df['price'], downcast='float')

# Use category dtype for repeated string values
df['city'] = df['city'].astype('category')  # Massive memory savings for low-cardinality strings

# Read large CSVs in chunks
for chunk in pd.read_csv('huge_file.csv', chunksize=10000):
    process(chunk)
```

### 10.3 Parallel Processing

```python
from sklearn.ensemble import RandomForestClassifier

# Use all CPU cores
model = RandomForestClassifier(n_jobs=-1)   # -1 = use all available cores

# GridSearchCV can also parallelize
from sklearn.model_selection import GridSearchCV
grid_search = GridSearchCV(model, param_grid, cv=5, n_jobs=-1)
```

---

## 11. Complete Cheat Sheet

### NumPy Quick Reference

```python
np.array([1, 2, 3])              # Create array
np.zeros((3, 3))                 # Zeros matrix
np.arange(0, 10, 2)               # Range with step
np.linspace(0, 1, 5)              # Evenly spaced values
np.random.seed(42)                 # Reproducibility
arr.shape                          # Dimensions
arr.reshape(3, -1)                # Reshape
arr[arr > 5]                       # Boolean filtering
arr.sum(), arr.mean(), arr.std()  # Aggregations
A @ B                               # Matrix multiplication
```

### Pandas Quick Reference

```python
pd.read_csv('file.csv')            # Load data
df.head(), df.info(), df.describe()  # Explore
df['col'], df[['a', 'b']]          # Select columns
df.loc[...], df.iloc[...]          # Select rows
df[df['col'] > 5]                  # Filter
df.groupby('col').mean()           # Group & aggregate
df.merge(df2, on='key')            # Join
df.fillna(0), df.dropna()          # Handle missing data
df.apply(func)                     # Apply function
df.to_csv('out.csv')               # Export
```

### Scikit-learn Quick Reference

```python
train_test_split(X, y, test_size=0.2)   # Split data
StandardScaler().fit_transform(X)        # Scale features
model.fit(X_train, y_train)              # Train
model.predict(X_test)                    # Predict
accuracy_score(y_test, predictions)      # Evaluate
cross_val_score(model, X, y, cv=5)       # Cross-validation
GridSearchCV(model, param_grid, cv=5)    # Tune hyperparameters
joblib.dump(model, 'model.pkl')          # Save model
```

### Matplotlib/Seaborn Quick Reference

```python
plt.plot(x, y)                     # Line plot
plt.scatter(x, y)                  # Scatter plot
plt.hist(data, bins=20)            # Histogram
sns.heatmap(df.corr(), annot=True) # Correlation heatmap
sns.boxplot(x='cat', y='val', data=df)  # Box plot
plt.savefig('output.png')          # Save figure
```

---

## Summary: Your Learning Path

```
1. Master NumPy basics       → Array creation, indexing, vectorization
2. Master Pandas             → Loading, filtering, grouping, merging
3. Learn visualization       → Matplotlib basics, then Seaborn for statistics
4. Learn scikit-learn        → Train/test split, preprocessing, models, evaluation
5. Combine with Flask        → Serve predictions via API endpoints
6. Add testing               → Sanity-check both data pipelines and model outputs
7. Optimize                  → Vectorize operations, manage memory, parallelize
```

**Practice progression:**

1. Load a CSV with Pandas, explore it, clean it
2. Visualize distributions and relationships
3. Train a simple classifier (Logistic Regression) on it
4. Improve with Random Forest + hyperparameter tuning
5. Wrap the trained model in a Flask `/predict` endpoint
6. Write unit tests for both your data cleaning functions and your endpoint
