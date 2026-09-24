CXX := g++
CXXFLAGS := -std=c++17 -Wall -Wextra -Wpedantic -O2

TARGET := quantum-parser
SOURCE := main.cpp

.PHONY: all clean run

all: $(TARGET)

$(TARGET): $(SOURCE)
	$(CXX) $(CXXFLAGS) $(SOURCE) -o $(TARGET)

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f $(TARGET)

Then:
make

Run it:
make run

Clean the compiled binary:
make clean

Or compile manually:
g++ -std=c++17 -Wall -Wextra -Wpedantic -O2 main.cpp -o quantum-parser
