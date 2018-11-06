sealed class Plot {
    class Type_section : Plot {
        var section: String
        var level: String
    }

    class Type_text : Plot {
        var role: String
        var text: String
    }

    class Type_instruction : Plot {
        var instruction: String
    }
}