#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <complex>
#include <stdexcept>

using Complex = std::complex<double>;

struct QuantumState {
    std::vector<Complex> amplitudes;
};

class QuantumStateParser {
public:
    static QuantumState parse(const std::string& input) {
        std::istringstream stream(input);

        QuantumState state;
        std::string token;

        while (stream >> token) {
            state.amplitudes.push_back(parseComplex(token));
        }

        if (state.amplitudes.empty()) {
            throw std::invalid_argument("Quantum state is empty");
        }

        return state;
    }

private:
    static Complex parseComplex(const std::string& token) {
        // Format examples:
        // 1
        // 0
        // 0.707+0.707i

        if (token.back() != 'i') {
            return Complex(std::stod(token), 0.0);
        }

        std::string value = token.substr(0, token.size() - 1);

        std::size_t split = std::string::npos;

        for (std::size_t i = 1; i < value.size(); ++i) {
            if (value[i] == '+' || value[i] == '-') {
                split = i;
                break;
            }
        }

        if (split == std::string::npos) {
            return Complex(0.0, std::stod(value));
        }

        double real = std::stod(value.substr(0, split));
        double imag = std::stod(value.substr(split));

        return Complex(real, imag);
    }
};

int main() {
    try {
        auto state =
            QuantumStateParser::parse(
                "0.707+0.707i 0 0 0.707-0.707i"
            );

        std::cout << "Parsed quantum state:\n";

        for (std::size_t i = 0;
             i < state.amplitudes.size();
             ++i) {

            const auto& amplitude = state.amplitudes[i];

            std::cout
                << "|" << i << "> = "
                << amplitude.real()
                << " + "
                << amplitude.imag()
                << "i\n";
        }

    } catch (const std::exception& e) {
        std::cerr << "Parse error: "
                  << e.what()
                  << '\n';

        return 1;
    }
}
