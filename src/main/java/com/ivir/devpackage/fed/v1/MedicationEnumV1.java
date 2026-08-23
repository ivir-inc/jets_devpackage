/*
 * Copyright 2026 IVIR Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivir.devpackage.fed.v1;

import java.util.Optional;

import static com.ivir.devpackage.fed.v1.MedicationQualities.*;

/**
 * this is a copy of the FOM MedicationEnum from V1.
 */
public enum MedicationEnumV1 {
        /** <code>saline09</code> (with ordinal 1) */
        SALINE09("saline 0.9%", 1, ISOTONIC),
        /** <code>saline3</code> (with ordinal 2) */
        SALINE3("saline 3%", 2, HYPERTONIC),
        /** <code>morphine</code> (with ordinal 3) */
        MORPHINE("morphine", 3, ANALGESICS),
        /** <code>ketamine</code> (with ordinal 4) */
        KETAMINE("ketamine", 4, ANALGESICS),
        /** <code>epinephrine</code> (with ordinal 5) */
        EPINEPHRINE("epinephrine", 5, ANAPHYLAXIS_TREATMENT),
        /** <code>dextrose50</code> (with ordinal 6) */
        DEXTROSE50("dextrose 50%", 6, HYPERTONIC),
        /** <code>lactatedRingers</code> (with ordinal 7) */
        LACTATED_RINGERS("lactated ringers", 7, ISOTONIC),
        /** <code>phenylephrineHcl</code> (with ordinal 8) */
        PHENYLEPHRINE_HCL("phenylephrine HCl", 8, RESPIRATORY_TREATMENT),
        /** <code>oxygen</code> (with ordinal 9) */
        OXYGEN("oxygen", 9, RESPIRATORY_TREATMENT),
        /** <code>hydromorphoneHcl</code> (with ordinal 10) */
        HYDROMORPHONE_HCL("hydromorphone HCl", 10, ANALGESICS),
        /** <code>promethazineHcl</code> (with ordinal 11) */
        PROMETHAZINE_HCL("promethazine HCl", 11, ANTI_EMETICS),
        /** <code>aspirin</code> (with ordinal 12) */
        ASPIRIN("aspirin", 12, ANALGESICS),
        /** <code>scopolamineHydrobrom</code> (with ordinal 13) */
        SCOPOLAMINE_HYDROBROM("scopolamine hydrobrom", 13, ANTI_EMETICS),
        /** <code>ranitidine</code> (with ordinal 14) */
        RANITIDINE("ranitidine", 14, ANTI_EMETICS),
        /** <code>ceftriaxoneSodium</code> (with ordinal 15) */
        CEFTRIAXONE_SODIUM("ceftriaxone sodium", 15, ANTIBIOTIC),
        /** <code>vecuronium</code> (with ordinal 16) */
        VECURONIUM("vecuronium", 16, PARALYTICS),
        /** <code>midazolamHcl</code> (with ordinal 17) */
        MIDAZOLAM_HCL("midazolam HCl", 17, SEDATION),
        /** <code>sodiumChloride</code> (with ordinal 18) */
        SODIUM_CHLORIDE("sodium chloride", 18, NUTRITION),
        /** <code>ketamineHydrochloride</code> (with ordinal 19) */
        KETAMINE_HYDROCHLORIDE("ketamine hydrochloride", 19, SEDATION),
        /** <code>levofloxacin</code> (with ordinal 20) */
        LEVOFLOXACIN("levofloxacin", 20, ANTIBIOTIC),
        /** <code>nitroglycerin</code> (with ordinal 21) */
        NITROGLYCERIN("nitroglycerin", 21, PRESSERS),
        /** <code>magnesiumSulfate</code> (with ordinal 22) */
        MAGNESIUM_SULFATE("magnesium sulfate", 22, NUTRITION),
        /** <code>ertapenemSodium</code> (with ordinal 23) */
        ERTAPENEM_SODIUM("ertapenem sodium", 23, ANTIBIOTIC),
        /** <code>calciumChloride</code> (with ordinal 24) */
        CALCIUM_CHLORIDE("calcium chloride", 24, POSITIVE_INOTROPIC),
        /** <code>rocuroniumBromide</code> (with ordinal 25) */
        ROCURONIUM_BROMIDE("rocuronium bromide", 25, SEDATION),
        /** <code>diphenhydramineHcl</code> (with ordinal 26) */
        DIPHENHYDRAMINE_HCL("diphenhydramine HCl", 26),
        /** <code>lidocaineHydrochloride</code> (with ordinal 27) */
        LIDOCAINE_HYDROCHLORIDE("lidocaine hydrochloride", 27, ANALGESICS),
        /** <code>amiodaroneHcl</code> (with ordinal 28) */
        AMIODARONE_HCL("amiodarone HCl", 28, NEGATIVE_INOTROPIC),
        /** <code>adenosine</code> (with ordinal 29) */
        ADENOSINE("adenosine", 29, NEGATIVE_INOTROPIC),
        /** <code>atropineSulfate</code> (with ordinal 30) */
        ATROPINE_SULFATE("atropine sulfate", 30),
        /** <code>etomidate</code> (with ordinal 31) */
        ETOMIDATE("etomidate", 31, SEDATION),
        /** <code>albuterolSulf</code> (with ordinal 32) */
        ALBUTEROL_SULF("albuterol sulfate", 32, RESPIRATORY_TREATMENT),
        /** <code>fentanylCitrate</code> (with ordinal 33) */
        FENTANYL_CITRATE("fentanyl citrate", 33, ANALGESICS),
        /** <code>ondansetron</code> (with ordinal 34) */
        ONDANSETRON("ondansetron", 34, ANTI_EMETICS),
        /** <code>ondansetronHcl</code> (with ordinal 35) */
        ONDANSETRON_HCL("ondansetron HCl", 35, ANTI_EMETICS),
        /** <code>levetiracetam</code> (with ordinal 36) */
        LEVETIRACETAM("levetiracetam", 36, SEIZURE_TREATMENT),
        /** <code>methylprednisolonesod</code> (with ordinal 37) */
        METHYLPREDNISOLONESOD("methylprednisolonesod", 37),
        /** <code>metoprololTartratein</code> (with ordinal 38) */
        METOPROLOL_TARTRATEIN("metoprolol tartratein", 38),
        /** <code>naloxoneHydrochlorid</code> (with ordinal 39) */
        NALOXONE_HYDROCHLORID("naloxone hydrochlorid", 39, OVERDOSE_TREATMENT),
        /** <code>norepinephrineBitart</code> (with ordinal 40) */
        NOREPINEPHRINE_BITART("norepinephrine bitartrate", 40, PRESSERS),
        /** <code>tranexamicAcid</code> (with ordinal 41) */
        TRANEXAMIC_ACID("tranexamic acid", 41, COAGULANT),
        /** <code>hextend</code> (with ordinal 42) */
        HEXTEND("hextend", 42, ISOTONIC),
        /** <code>freshWholeBlood</code> (with ordinal 43) */
        FRESH_WHOLE_BLOOD("fresh whole blood", 43, BLOOD_PRODUCTS),
        /** <code>plasma</code> (with ordinal 44) */
        PLASMA("plasma", 44, BLOOD_PRODUCTS),
        /** <code>redBloodCells</code> (with ordinal 45) */
        RED_BLOOD_CELLS("red blood cells", 45, BLOOD_PRODUCTS),
        /** <code>plasmalyteA</code> (with ordinal 46) */
        PLASMALYTE_A("plasmalyte A", 46, NUTRITION),
        /** <code>clindamycin</code> (with ordinal 47) */
        CLINDAMYCIN("clindamycin", 47, ANTIBIOTIC),
        /** <code>acetaminophen</code> (with ordinal 48) */
        ACETAMINOPHEN("acetaminophen", 48, ANALGESICS),
        /** <code>dailyMeal</code> (with ordinal 49) */
        DAILY_MEAL("daily meal", 49, NUTRITION),
        /** <code>glassOfWater</code> (with ordinal 50) */
        GLASS_OF_WATER("glass of water", 50, NUTRITION),
        /** <code>acyclovir</code> (with ordinal 51) */
        ACYCLOVIR("acyclovir", 51, ANTIVIRAL),
        /** <code>narcan</code> (with ordinal 52) */
        NARCAN("narcan", 52, OVERDOSE_TREATMENT),
        /** <code>ibuprofen</code> (with ordinal 53) */
        IBUPROFEN("ibuprofen", 53, ANALGESICS),
        /** <code>meloxicam</code> (with ordinal 54) */
        MELOXICAM("meloxicam", 54, ANALGESICS),
        /** <code>cefotetan</code> (with ordinal 55) */
        CEFOTETAN("cefotetan", 55, ANTIBIOTIC),
        /** <code>moxifloxacin</code> (with ordinal 56) */
        MOXIFLOXACIN("moxifloxacin", 56, ANTIBIOTIC);

        /**
         * The name of the enum.
         */
        public final String name;
        /**
         * The ordinal of the enum.
         */
        public final long ordinal;
        private MedicationQualities[] qualities = null;

        private MedicationEnumV1(String name, long ordinal, MedicationQualities ... qualities) {
            this.name = name;
            this.ordinal = ordinal;
            this.qualities = qualities;
        }

        public long getOrdinal() {
            return ordinal;
        }

        public String getName() {
            return name;
        }

        public MedicationQualities[] getQualities(){
                if(qualities == null){
                        return new MedicationQualities[0];
                }
                return this.qualities;
        }

        /**
         * Find the enum with the specified ordinal.
         *
         * @param ordinal ordinal of the enum to find
         *
         * @return the enum with the specified <code>ordinal</code>, or <code>null</code> if not found
         */
        public static MedicationEnumV1 find(long ordinal) {
            for (MedicationEnumV1 value : values()) {
                if (value.getOrdinal() == ordinal) {
                    return value;
                }
            }
            return null;
        }

        /**
         * Find the enum with the specified name.
         *
         * @param name name of the enum to find
         *
         * @return the enum with the specified <code>name</code>, or <code>null</code> if not found
         */
        public static MedicationEnumV1 findByName(String name) {
            for (MedicationEnumV1 value : values()) {
                if (value.getName().equals(name)) {
                    return value;
                }
            }
            return null;
        }

        public static Optional<MedicationEnumV1> findFirstContainsName(String name){
                name = name.toLowerCase();
                for (MedicationEnumV1 value : values()) {
                        if (value.getName().toLowerCase().contains(name)) {
                                return Optional.ofNullable(value);
                        }
                }
                return Optional.empty();
        }


        public static Optional<MedicationEnumV1> findByEnum(String enumStr){
            for (MedicationEnumV1 value : values()) {
                if (value.toString().equalsIgnoreCase(enumStr)) {
                    return Optional.ofNullable(value);
                }
            }
            return Optional.empty();
        }



    }
