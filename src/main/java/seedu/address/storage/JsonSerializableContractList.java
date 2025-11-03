package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.contract.Contract;
import seedu.address.model.contract.ContractList;
import seedu.address.model.contract.ReadOnlyContractList;

/**
 * An Immutable ContractList that is serializable to JSON format.
 */
@JsonRootName(value = "contractlist")
class JsonSerializableContractList {

    public static final String MESSAGE_DUPLICATE_CONTRACT = "Contracts list contains duplicate contract(s).";

    private final List<JsonAdaptedContract> contracts = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableContractList} with the given contracts.
     */
    @JsonCreator
    public JsonSerializableContractList(
            @JsonProperty("contracts") List<JsonAdaptedContract> contracts) {
        this.contracts.addAll(contracts);
    }

    /**
     * Converts a given {@code ReadOnlyContractList} into this class for Jackson use.
     * @param source future changes to this will not affect the created {@code JsonSerializableContractList}.
     */
    public JsonSerializableContractList(ReadOnlyContractList source) {
        contracts.addAll(source.getContractList().stream()
                .map(JsonAdaptedContract::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this list into the model's {@code ContractList} object.
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public ContractList toModelType() throws IllegalValueException {
        ContractList list = new ContractList();
        for (JsonAdaptedContract jsonContract : contracts) {
            Contract contract = jsonContract.toModelType();
            if (list.hasContract(contract)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CONTRACT);
            }
            list.addContract(contract);
        }
        return list;
    }
}
