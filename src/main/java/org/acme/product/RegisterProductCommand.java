package org.acme.product;

import org.acme.core.cqrsedaes.cqrs.BaseCommand;

public class RegisterProductCommand extends BaseCommand<RegisterProductDto> {

    private RegisterProductCommand() {
    }

    public static RegisterProductCommand fromDto(RegisterProductDto dto) {
        return (RegisterProductCommand) new RegisterProductCommand().withData(dto);
    }
}
