package com.lucy.baseremote.enums

enum class PowerPackLocation {
    OldBase, NewBase, Chem
}

enum class MELocation {
    OldBase
}

enum class PowerGenLocation {
    Dam11, Dam12, Dam13, Dam14, Dam15, Dam16, Dam17,
    Dam21, Dam22, Dam23, Dam24, Dam25, Dam26, Dam27,

    ChemN1, ChemN2, ChemE1, ChemE2, ChemS1, ChemS2, ChemW1, ChemW2,

    MountainWind;
}

enum class PowerGenType {
    Hydro, Wind, Solar
}