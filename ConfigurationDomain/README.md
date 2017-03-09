# ConfigurationDomain
This module defines UseCases and Repository interfaces to implement following product specifications.

## Product specifications
* The Configuration feature mainly offers capability to retrieve available regions, their server host names and available transport modes.
* The Configuration feature also provides detailed information about covered transport service providers (TSP) for a specified region.
* In order to retrieve correct regions and correct transport modes, a region eligibility must be given and its value will be added into headers of all network requests.

## Server APIs
Please refer to [the Configuration section](https://skedgo.github.io/tripgo-api/#tag/Configuration).
