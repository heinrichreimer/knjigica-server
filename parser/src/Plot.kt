sealed class Plot {
    class Section : Plot {
        var section: String
        var level: String
    }

    class Text : Plot {
        var role: String
        var text: String
    }

    class Instruction : Plot {
        var instruction: String
    }
}