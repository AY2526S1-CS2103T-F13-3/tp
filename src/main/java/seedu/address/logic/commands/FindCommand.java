package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.model.Model;
import seedu.address.model.athlete.Athlete;
import seedu.address.model.contract.Contract;
import seedu.address.model.organization.Organization;

/**
 * Filters and shows model entities based on query keywords using case-insensitive fuzzy matches.
 * The command temporarily filters the in-memory lists without mutating any saved data.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters the requested list by keyword (case-insensitive, allows close matches).\n"
            + "Exactly one flag must be supplied:\n"
            + "  an/ KEYWORD    Shows athletes whose name closely matches KEYWORD.\n"
            + "  as/ KEYWORD    Shows athletes whose sport closely matches KEYWORD.\n"
            + "  on/ KEYWORD    Shows organizations whose name closely matches KEYWORD.\n"
            + "  ca/ KEYWORD    Shows contracts whose athlete's name closely matches KEYWORD.\n"
            + "  co/ KEYWORD    Shows contracts whose organization name closely matches KEYWORD.\n"
            + "  cs/ KEYWORD    Shows contracts whose sport closely matches KEYWORD.\n"
            + "Example: " + COMMAND_WORD + " an/ Lionel";

    private static final String MESSAGE_RESULTS_FORMAT = "Showing %1$d %2$s closely matching \"%3$s\".\n"
            + "You can refresh the display anytime using Cmd+R (macOS) "
            + "or Ctrl+R (Windows/Linux) to show all data again.";
    private static final String MESSAGE_NO_RESULTS_FORMAT = "No %1$s found matching \"%2$s\".\n"
            + "You can refresh the display anytime using Cmd+R (macOS) "
            + "or Ctrl+R (Windows/Linux) to show all data again.";

    /**
     * Supported scopes for the find command.
     */
    public enum SearchScope {
        ATHLETE_NAME("athletes", "athlete", CommandResult.UiTab.ATHLETES) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredAthleteList(athleteMatchesName(keywordLower));
                model.updateFilteredOrganizationList(Model.PREDICATE_SHOW_ALL_ORGANIZATIONS);
                model.updateFilteredContractList(Model.PREDICATE_SHOW_ALL_CONTRACTS);
                return model.getFilteredAthleteList().size();
            }
        },
        ATHLETE_SPORT("athletes", "athlete", CommandResult.UiTab.ATHLETES) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredAthleteList(athleteMatchesSport(keywordLower));
                model.updateFilteredOrganizationList(Model.PREDICATE_SHOW_ALL_ORGANIZATIONS);
                model.updateFilteredContractList(Model.PREDICATE_SHOW_ALL_CONTRACTS);
                return model.getFilteredAthleteList().size();
            }
        },
        ORGANIZATION_NAME("organizations", "organization", CommandResult.UiTab.ORGANIZATIONS) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredOrganizationList(organizationMatchesName(keywordLower));
                model.updateFilteredAthleteList(Model.PREDICATE_SHOW_ALL_ATHLETES);
                model.updateFilteredContractList(Model.PREDICATE_SHOW_ALL_CONTRACTS);
                return model.getFilteredOrganizationList().size();
            }
        },
        CONTRACT_ATHLETE("contracts", "contract", CommandResult.UiTab.CONTRACTS) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredContractList(contractMatchesAthleteName(keywordLower));
                model.updateFilteredAthleteList(Model.PREDICATE_SHOW_ALL_ATHLETES);
                model.updateFilteredOrganizationList(Model.PREDICATE_SHOW_ALL_ORGANIZATIONS);
                return model.getFilteredContractList().size();
            }
        },
        CONTRACT_ORGANIZATION("contracts", "contract", CommandResult.UiTab.CONTRACTS) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredContractList(contractMatchesOrganizationName(keywordLower));
                model.updateFilteredAthleteList(Model.PREDICATE_SHOW_ALL_ATHLETES);
                model.updateFilteredOrganizationList(Model.PREDICATE_SHOW_ALL_ORGANIZATIONS);
                return model.getFilteredContractList().size();
            }
        },
        CONTRACT_SPORT("contracts", "contract", CommandResult.UiTab.CONTRACTS) {
            @Override
            int apply(Model model, String keywordLower) {
                model.updateFilteredContractList(contractMatchesSport(keywordLower));
                model.updateFilteredAthleteList(Model.PREDICATE_SHOW_ALL_ATHLETES);
                model.updateFilteredOrganizationList(Model.PREDICATE_SHOW_ALL_ORGANIZATIONS);
                return model.getFilteredContractList().size();
            }
        };

        private final String noun;
        private final String singularNoun;
        private final CommandResult.UiTab tabToShow;

        SearchScope(String noun, String singularNoun, CommandResult.UiTab tabToShow) {
            this.noun = noun;
            this.singularNoun = singularNoun;
            this.tabToShow = tabToShow;
        }

        abstract int apply(Model model, String keywordLower);

        CommandResult.UiTab getTabToShow() {
            return tabToShow;
        }

        /**
         * Returns the plural noun for this search scope.
         *
         * @return The plural noun.
         */
        String getNoun() {
            return noun;
        }

        /**
         * Returns the appropriate noun form based on the count.
         *
         * @param count The number of items.
         * @return The singular noun if count is 1, otherwise the plural noun.
         */
        String getNoun(int count) {
            return count == 1 ? getSingularNoun() : noun;
        }

        /**
         * Returns the singular noun for this search scope.
         *
         * @return The singular noun.
         */
        String getSingularNoun() {
            switch (this) {
            case ATHLETE_NAME:
            case ATHLETE_SPORT:
                return "athlete";
            case ORGANIZATION_NAME:
                return "organization";
            case CONTRACT_ATHLETE:
            case CONTRACT_ORGANIZATION:
            case CONTRACT_SPORT:
                return "contract";
            default:
                return noun; // fallback
            }
        }
    }

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{Alnum}]+");

    private final SearchScope scope;
    private final String keyword;

    /**
     * Constructs a {@code FindCommand} that filters according to {@code scope} and {@code keyword}.
     */
    public FindCommand(SearchScope scope, String keyword) {
        requireNonNull(scope);
        requireNonNull(keyword);
        this.scope = scope;
        this.keyword = keyword;
    }

    /**
     * Executes the find command to filter entities in the model.
     * Uses fuzzy matching to find entities that closely match the keyword.
     *
     * @param model The model to search in. Cannot be null.
     * @return A CommandResult with feedback about the number of matches found.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        String keywordLower = keyword.toLowerCase();
        int matches = scope.apply(model, keywordLower);

        String feedback;
        if (matches == 0) {
            feedback = String.format(MESSAGE_NO_RESULTS_FORMAT, scope.getNoun(), keyword);
        } else {
            feedback = String.format(MESSAGE_RESULTS_FORMAT, matches, scope.getNoun(matches), keyword);
        }

        return new CommandResult(feedback, scope.getTabToShow());
    }

    /**
     * Returns true if both FindCommand objects have the same scope and keyword (case-insensitive).
     *
     * @param other The other object to compare with.
     * @return True if both objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherCommand = (FindCommand) other;
        return scope == otherCommand.scope && keyword.equalsIgnoreCase(otherCommand.keyword);
    }

    /**
     * Returns the hash code for this FindCommand.
     *
     * @return The hash code based on scope and lowercase keyword.
     */
    @Override
    public int hashCode() {
        return (scope.name() + "|" + keyword.toLowerCase()).hashCode();
    }

    /**
     * Performs fuzzy matching between the search keyword and target text.
     * Uses a three-tier matching strategy:
     * 1. Exact substring match (case-insensitive)
     * 2. Levenshtein distance matching on the full text
     * 3. Word-by-word Levenshtein distance matching
     *
     * @param haystack the text to search in
     * @param needleLower the search keyword (must be lowercase)
     * @return true if the keyword fuzzy-matches the haystack text
     */
    private static boolean matchesFuzzy(String haystack, String needleLower) {
        String haystackLower = haystack.toLowerCase();

        // Tier 1: Exact substring match (fastest)
        if (haystackLower.contains(needleLower)) {
            return true;
        }

        // Calculate allowed edit distance based on keyword length
        int allowedDistance = allowableDistance(needleLower.length());
        if (allowedDistance == 0) {
            return false; // Too short for fuzzy matching
        }

        // Tier 2: Full text Levenshtein distance match
        if (levenshteinDistance(haystackLower, needleLower) <= allowedDistance) {
            return true;
        }

        // Tier 3: Word-by-word Levenshtein distance match
        // Splits text into words and tries matching each word individually
        Matcher matcher = WORD_PATTERN.matcher(haystackLower);
        while (matcher.find()) {
            String token = matcher.group();
            if (levenshteinDistance(token, needleLower) <= allowedDistance) {
                return true;
            }
        }

        return false;
    }

    /**
     * Calculates the maximum allowed Levenshtein distance for fuzzy matching
     * based on the keyword length. Shorter keywords require exact matches,
     * while longer keywords allow more edits.
     *
     * @param length the length of the search keyword
     * @return the maximum allowed edit distance
     */
    private static int allowableDistance(int length) {
        if (length <= 2) {
            return 0; // 1-2 chars: exact match only
        }
        if (length <= 4) {
            return 1; // 3-4 chars: 1 edit allowed
        }
        if (length <= 7) {
            return 2; // 5-7 chars: 2 edits allowed
        }
        if (length <= 11) {
            return 3; // 8-11 chars: 3 edits allowed
        }
        return 4; // 12+ chars: 4 edits allowed
    }

    /**
     * Calculates the Levenshtein distance (edit distance) between two strings.
     * The edit distance is the minimum number of single-character edits
     * (insertions, deletions, or substitutions) required to change one string
     * into another.
     *
     * Uses dynamic programming with space optimization (O(min(m,n)) space).
     *
     * @param left the first string
     * @param right the second string
     * @return the minimum edit distance between the strings
     */
    private static int levenshteinDistance(String left, String right) {
        if (left.isEmpty()) {
            return right.length();
        }
        if (right.isEmpty()) {
            return left.length();
        }

        int[] previous = new int[right.length() + 1];
        int[] current = new int[right.length() + 1];

        for (int j = 0; j <= right.length(); j++) {
            previous[j] = j;
        }

        for (int i = 1; i <= left.length(); i++) {
            current[0] = i;
            char leftChar = left.charAt(i - 1);
            for (int j = 1; j <= right.length(); j++) {
                int cost = leftChar == right.charAt(j - 1) ? 0 : 1;
                current[j] = Math.min(Math.min(current[j - 1] + 1, previous[j] + 1), previous[j - 1] + cost);
            }
            int[] temp = previous;
            previous = current;
            current = temp;
        }

        return previous[right.length()];
    }

    private static java.util.function.Predicate<Athlete> athleteMatchesName(String keywordLower) {
        return athlete -> matchesFuzzy(athlete.getName().toString(), keywordLower);
    }

    private static java.util.function.Predicate<Athlete> athleteMatchesSport(String keywordLower) {
        return athlete -> matchesFuzzy(athlete.getSport().toString(), keywordLower);
    }

    private static java.util.function.Predicate<Organization> organizationMatchesName(String keywordLower) {
        return organization -> matchesFuzzy(organization.getName().toString(), keywordLower);
    }

    private static java.util.function.Predicate<Contract> contractMatchesAthleteName(String keywordLower) {
        return contract -> matchesFuzzy(contract.getAthlete().getName().toString(), keywordLower);
    }

    private static java.util.function.Predicate<Contract> contractMatchesOrganizationName(String keywordLower) {
        return contract -> matchesFuzzy(contract.getOrganization().getName().toString(), keywordLower);
    }

    private static java.util.function.Predicate<Contract> contractMatchesSport(String keywordLower) {
        return contract -> matchesFuzzy(contract.getSport().toString(), keywordLower);
    }
}
